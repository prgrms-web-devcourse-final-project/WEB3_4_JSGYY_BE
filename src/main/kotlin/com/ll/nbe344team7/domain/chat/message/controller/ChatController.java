package com.ll.nbe344team7.domain.chat.message.controller;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatMessageService chatMessageService;


    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/message")
    public void sendMessage(
            @RequestBody MessageDTO messageDTO,
            Principal principal
    ) {
        if (principal != null) {
            try {
                if (principal instanceof UsernamePasswordAuthenticationToken auth){
                    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                    chatMessageService.send(messageDTO, userDetails.getMemberId());
                }
            } catch (Exception e) {
                log.error("Chat Publish Error: ", e);
            }
        }
        else  {
            log.error("Principal is null");
        }
    }
}
