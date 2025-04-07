package com.ll.nbe344team7.global.config.webSocket.handler.command;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface StompCommandHandler {
    void handle(StompHeaderAccessor accessor);
}
