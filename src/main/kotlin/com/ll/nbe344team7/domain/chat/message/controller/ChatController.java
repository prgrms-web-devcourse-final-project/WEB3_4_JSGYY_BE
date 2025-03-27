package com.ll.nbe344team7.domain.chat.message.controller;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/rooms/{roomId}")
public class ChatController {

    private final SimpMessagingTemplate template;

    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/chat/message")
    public void sendMessage(@RequestBody MessageDTO messageDTO) {

        template.convertAndSend("/sub/chat/room/" + messageDTO.getRoomId(), messageDTO);
    }
}
