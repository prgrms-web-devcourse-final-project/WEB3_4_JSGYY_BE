package com.ll.nbe344team7.global.config.webSocket.handler.command;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * 웹 소캣 연결 종료 명령
 *
 * @author user
 * @since 25. 4. 7.
 */
@Component
public class DisConnectHandler implements StompCommandHandler{

    @Override
    public void handle(StompHeaderAccessor accessor) {
        accessor.getSessionAttributes().clear();
    }
}
