package com.ll.nbe344team7.domain.chat.message.controller;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/rooms/{roomId}")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final RedisTemplate<String, MessageDTO> redisTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(RedisTemplate<String, MessageDTO> redisTemplate, ChatMessageService chatMessageService) {
        this.redisTemplate = redisTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/message")
    public void sendMessage(
            @RequestBody MessageDTO messageDTO
            ) {
        try {
            chatMessageService.send(messageDTO, messageDTO.getRoomId());
        } catch (Exception e) {
            log.error("Chat Publish Error: ", e);
        }
    }
}
