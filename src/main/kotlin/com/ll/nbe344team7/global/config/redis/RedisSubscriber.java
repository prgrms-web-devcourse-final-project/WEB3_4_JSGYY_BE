package com.ll.nbe344team7.global.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RedisSubscriber implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(RedisSubscriber.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatMessageDTO chatMessageDTO = objectMapper.readValue(message.getBody(), ChatMessageDTO.class);
            log.info("Received message from Redis: {}", chatMessageDTO);
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageDTO.getChatroomId(), chatMessageDTO);
        } catch (IOException e) {
            log.error("Redis Subscriber Error: ", e);
        }
    }
}
