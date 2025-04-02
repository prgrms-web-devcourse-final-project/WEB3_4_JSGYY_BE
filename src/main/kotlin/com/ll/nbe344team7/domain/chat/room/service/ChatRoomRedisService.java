package com.ll.nbe344team7.domain.chat.room.service;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.repository.ChatParticipantRepository;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.repository.ChatRoomRedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private final RedisTemplate<String, String> redisTemplate;

    public ChatRoomRedisService(ChatRoomRedisRepository chatRoomRedisRepository, ChatParticipantRepository chatParticipantRepository, RedisTemplate<String, String> redisTemplate) {
        this.chatRoomRedisRepository = chatRoomRedisRepository;
        this.chatParticipantRepository = chatParticipantRepository;
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
    public List<ChatRoomListResponseDto> getChatRooms(Long memberId) {
        List<ChatParticipant> participants = chatParticipantRepository.findByMemberId(memberId);
        List<ChatRoomListResponseDto> chatRoomList = new ArrayList<>();

        for (ChatParticipant participant : participants) {
            Long roomId = participant.getChatroom().getId();
            String title = participant.getChatroom().getTitle();
            String nickname = participant.getMember().getNickname();
            String lastMessage = getLastMessageFromRedis(roomId);

            chatRoomList.add(new ChatRoomListResponseDto(roomId, title, nickname, lastMessage));
        }

        chatRoomList.sort((a, b) -> {
            Long timeA = getLastMessageTimestamp(a.getId());
            Long timeB = getLastMessageTimestamp(b.getId());
            return Long.compare(timeB, timeA); // 최신순 정렬 (내림차순)
        });

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

        // 💡 ZSet에서 가장 최신(last) 메시지를 가져옴
        Set<String> messages = getZSetOperations().reverseRange(key, 0, 0);

        if (messages == null || messages.isEmpty()) return "";
        return messages.iterator().next(); // 가장 최신 메시지 반환
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

        // 💡 ZSet에서 가장 최신 메시지의 점수(타임스탬프)를 가져옴
        Set<ZSetOperations.TypedTuple<String>> messages = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 0);

        if (messages == null || messages.isEmpty()) return 0L;
        return messages.iterator().next().getScore().longValue(); // 가장 최신 메시지의 타임스탬프 반환
    }
}
