package com.ll.nbe344team7.global.config.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 발급된 ChatRoomList를 수신
 *
 * @author kjm72
 * @since 2025-04-03
 */
@Component
public class RedisChatRoomListSubscriber implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(RedisChatRoomListSubscriber.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisChatRoomListSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * /sub/chat/roomList/{userId}로 발급된 리스트 수신
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatRoomListDto chatRoomListMessage = objectMapper.readValue(message.getBody(), ChatRoomListDto.class);
            messagingTemplate.convertAndSend("/sub/chat/roomList/" + chatRoomListMessage.getMemberId(), chatRoomListMessage.getChatRooms());
        } catch (IOException e) {
            log.error("ChatRoomListSubscriber Error: ", e);
        }
    }

}
