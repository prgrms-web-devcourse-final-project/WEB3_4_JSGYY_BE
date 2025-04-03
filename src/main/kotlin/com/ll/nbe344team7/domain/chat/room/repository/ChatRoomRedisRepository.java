package com.ll.nbe344team7.domain.chat.room.repository;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 채팅룸, 메시지 RedisRepository
 *
 * @author kjm72
 * @since 2025-04-02
 */
@Repository
public class ChatRoomRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;


    public ChatRoomRedisRepository(RedisTemplate<String, String> redisTemplate, MemberRepository memberRepository) {
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
    }

    private ZSetOperations<String, String> getZSetOperations() {
        return redisTemplate.opsForZSet();
    }


    /**
     * 채팅방의 마지막 메시지를 저장
     * @param messageDTO
     *
     * @author kjm72
     * @since 2025-04-02
     */
    public void saveLastMessage(MessageDTO messageDTO, Long memberId) {
        Member member= memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        String value = messageDTO.getRoomId() + "|" +member.getNickname() + "|" + messageDTO.getContent() + "|" + LocalDateTime.now();
        getZSetOperations().add("chatroom:"+messageDTO.getRoomId()+":lastMessage", value, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }
}