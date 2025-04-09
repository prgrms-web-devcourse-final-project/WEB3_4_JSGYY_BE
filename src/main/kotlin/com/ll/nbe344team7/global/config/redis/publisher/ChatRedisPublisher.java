package com.ll.nbe344team7.global.config.redis.publisher;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.service.ChatRoomRedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 채팅 redis pub
 *
 * @author jyson
 * @since 25. 4. 7.
 */
@Component
public class ChatRedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRedisService chatRoomRedisService;

    public ChatRedisPublisher(RedisTemplate<String, Object> redisTemplate, ChatRoomRedisService chatRoomRedisService) {
        this.redisTemplate = redisTemplate;
        this.chatRoomRedisService = chatRoomRedisService;
    }

    public void publishMessage(MessageDTO dto, ChatMessage chatMessage) {
        redisTemplate.convertAndSend("chatroom", new ChatMessageDTO(chatMessage));
        chatRoomRedisService.saveLastMessage(dto, chatMessage.getMember().getId());
    }

    public void publishRoomListUpdate(Long memberId, List<ChatParticipant> chatParticipants) {
        for (ChatParticipant chatParticipant : chatParticipants) {
            Long participantId = chatParticipant.getMember().getId();
            List<ChatRoomListResponseDto> chatRoomList = chatRoomRedisService.getChatRooms(participantId);
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(memberId, chatRoomList);
            redisTemplate.convertAndSend("chatroomList", chatRoomListDto);
        }
    }
}
