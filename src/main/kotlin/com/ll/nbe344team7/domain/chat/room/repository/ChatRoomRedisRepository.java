package com.ll.nbe344team7.domain.chat.room.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 채팅룸, 메시지 RedisRepository
 *
 * @author kjm72
 * @since 2025-04-02
 */
@Repository
public class ChatRoomRedisRepository {
    private static final Logger log = LoggerFactory.getLogger(ChatRoomRedisRepository.class);
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;


    public ChatRoomRedisRepository(RedisTemplate<String, String> redisTemplate, MemberRepository memberRepository, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
    }



    /**
     * 채팅방의 마지막 메시지를 저장
     *
     * @param messageDTO
     * @author kjm72
     * @since 2025-04-02
     */
    public void saveLastMessage(MessageDTO messageDTO, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        String key = "chatroom:" + messageDTO.getRoomId() + ":lastMessage";
        // Hash에 개별 필드 저장
        redisTemplate.opsForHash().put(key, "roomId", messageDTO.getRoomId().toString());
        redisTemplate.opsForHash().put(key, "nickname", member.getNickname());
        redisTemplate.opsForHash().put(key, "content", messageDTO.getContent());
        redisTemplate.opsForHash().put(key, "timestamp", String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
    }

    public void saveChatRoomList(Long memberId, List<ChatRoomListResponseDto> chatRoomList) {
        String key = "chatroom:roomList:" + memberId;
        try {
            // List -> JSON 변환
            String jsonValue = objectMapper.writeValueAsString(chatRoomList);

            // Redis 저장
            redisTemplate.opsForValue().set(key, jsonValue);
            redisTemplate.convertAndSend("chatroom:roomList", jsonValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis 저장 중 JSON 변환 오류 발생", e);
        }
    }

    public List<ChatRoomListResponseDto> getChatRoomList(Long memberId) {
        String key = "chatroom:roomList:" + memberId;
        String value = (String) redisTemplate.opsForValue().get(key);

        if (value != null && !value.isEmpty()) {
            try {
                return objectMapper.readValue(value,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, ChatRoomListResponseDto.class));
            } catch (Exception e) {
                log.error("채팅방 목록 조회 실패: {}", e.getMessage(), e);
            }
        }

        return null;
    }

    public void deleteChatRoomList(Long participantId) {
        String key = "chatroom:roomList:" + participantId;

        // Redis에서 해당 참가자의 채팅방 리스트 삭제
        Boolean isDeleted = redisTemplate.delete(key);

        if (Boolean.TRUE.equals(isDeleted)) {
            System.out.println("✅ [디버깅] 채팅방 리스트 삭제 완료: participantId=" + participantId);
        } else {
            System.out.println("⚠️ [디버깅] 삭제할 채팅방 리스트 없음: participantId=" + participantId);
        }
    }
}