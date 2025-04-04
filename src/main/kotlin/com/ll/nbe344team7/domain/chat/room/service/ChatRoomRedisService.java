package com.ll.nbe344team7.domain.chat.room.service;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.message.repository.ChatMessageRepository;
import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.repository.ChatParticipantRepository;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.repository.ChatRoomRedisRepository;
import com.ll.nbe344team7.global.exception.ChatRoomException;
import com.ll.nbe344team7.global.exception.ChatRoomExceptionCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * 채팅 Redis 서비스
 *
 * @author kjm72
 * @since 2025-04-02
 */
@Service
public class ChatRoomRedisService {

    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public ChatRoomRedisService(ChatRoomRedisRepository chatRoomRedisRepository, ChatParticipantRepository chatParticipantRepository, ChatMessageRepository chatMessageRepository, RedisTemplate<String, String> redisTemplate) {
        this.chatRoomRedisRepository = chatRoomRedisRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.redisTemplate = redisTemplate;
    }

    private ZSetOperations<String, String> getZSetOperations() {
        return redisTemplate.opsForZSet();
    }

    /**
     * 마지막 채팅 메시지 저장
     * @param messageDTO
     *
     * @author kjm72
     * @since 2025-04-02
     */
    public void saveLastMessage(MessageDTO messageDTO, Long memberId) {
        chatRoomRedisRepository.saveLastMessage(messageDTO,memberId);
    }

    /**
     * 채팅방 목록 최신순으로 조회
     * MySQL에서 유저가 속한 채팅방 목록 조회
     * 각 채팅방의 최신 메시지를 Redis에서 가져와 DTO로 변환
     * 최신 메시지 기준으로 정렬 (Redis에 저장된 최신 메시지의 타임스탬프 사용)
     *
     * @return
     *
     * @author kjm72
     * @since 2025-04-02
     */
    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDto> getChatRooms(Long memberId) {
        // member가 참여한 채팅방 목록을 가져옴
        List<ChatParticipant> participants = chatParticipantRepository.findByMemberId(memberId);
        List<ChatRoomListResponseDto> chatRoomList = new ArrayList<>();
        List<ChatRoomListResponseDto> cachedRoomList = chatRoomRedisRepository.getChatRoomList(memberId);
        if (cachedRoomList != null && !cachedRoomList.isEmpty()) {
            return cachedRoomList;
        }

        if (participants.isEmpty()){
            throw new ChatRoomException(ChatRoomExceptionCode.NOT_FOUND_LIST);
        }

        // 채팅방을 담을 목록 생성

        // 사용자가 참여한 채팅방들을 조회하면서 정보를 가져옴
        for (ChatParticipant participant : participants) {
            Long roomId = participant.getChatroom().getId();
            String title = participant.getChatroom().getTitle();
            String nickname = getLastMessageSenderNicknameFromRedis(roomId);
            String lastMessage = getLastMessageFromRedis(roomId);

            // lastMessage가 Redis에 없을 경우 DB에서 가져와 Redis에 저장
            if (lastMessage.isBlank()){
                ChatMessage chatRoomLastMessage = chatMessageRepository.findLastMessageByRoomId(roomId);
                if (chatRoomLastMessage != null) {
                    lastMessage = chatRoomLastMessage.getContent();
                    updateLastMessageInRedis(roomId, lastMessage, chatRoomLastMessage.getCreatedAt());
                } else {
                    lastMessage = ""; // 기본값 설정
                }
            }
            // 채팅방 목록에 추가
            chatRoomList.add(new ChatRoomListResponseDto(roomId, title, nickname, lastMessage));
        }

        // 채팅방 최신순으로 정렬(전체를 도는 코드)
        chatRoomList.sort((a, b) -> {
            Long timeA = getLastMessageTimestamp(a.getId());
            Long timeB = getLastMessageTimestamp(b.getId());
            return Long.compare(timeB, timeA); // 최신순 정렬 (내림차순)
        });

        // Redis에 저장
        chatRoomRedisRepository.saveChatRoomList(memberId,chatRoomList);
        return chatRoomList;
    }

    /**
     * Redis에서 마지막 메세지 호출
     * @param roomId
     * @return
     *
     * @author kjm72
     * @since 2025-04-02
     */
    private String getLastMessageFromRedis(Long roomId) {
        String key = "chatroom:" + roomId + ":lastMessage";
        Object lastMessage = redisTemplate.opsForHash().get(key, "content");

        if (lastMessage == null) {
            ChatMessage chatRoomLastMessage = chatMessageRepository.findLastMessageByRoomId(roomId);
            if (chatRoomLastMessage != null) {
                updateLastMessageInRedis(roomId, chatRoomLastMessage.getContent(), chatRoomLastMessage.getCreatedAt());
                return chatRoomLastMessage.getContent();
            }
            return "";
        }
        return lastMessage.toString();
    }

    /**
     * Redis에서 마지막 메시지의 create_at 호출
     * @param roomId
     * @return
     *
     * @author kjm72
     * @since 2025-04-02
     */
    private Long getLastMessageTimestamp(Long roomId) {
        String key = "chatroom:" + roomId + ":lastMessage";
        Object timestamp = redisTemplate.opsForHash().get(key, "timestamp");
        return timestamp != null ? Long.parseLong(timestamp.toString()) : 0L;
    }

    /**
     * Redis에서 마지막 메시지 전송자 닉네임을 호출
     * @param roomId
     * @return
     *
     * @author kjm72
     * @since 2025-04-04
     */
    private String getLastMessageSenderNicknameFromRedis(Long roomId) {
        String key = "chatroom:" + roomId + ":lastMessage";
        Object nickname = redisTemplate.opsForHash().get(key, "nickname");
        return nickname != null ? nickname.toString() : "알 수 없음";
    }

    /**
     * 마지막 메시지 업데이트
     * @param roomId
     * @param content
     * @param createdAt
     *
     * @author kjm72
     * @since 2025-04-04
     */
    private void updateLastMessageInRedis(Long roomId, String content, LocalDateTime createdAt) {
        String key = "chatroom:" + roomId + ":lastMessage";
        Long timestamp = createdAt.toEpochSecond(ZoneOffset.UTC);
        redisTemplate.opsForHash().put(key, "content", content);
        redisTemplate.opsForHash().put(key, "timestamp", timestamp.toString());
    }
}
