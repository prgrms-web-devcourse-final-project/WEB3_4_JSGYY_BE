package com.ll.nbe344team7.global.config.webSocket.handler;

import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    public StompHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) { // WebSocket 연결 요청 시

            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            Cookie[] cookies = (Cookie[]) sessionAttributes.get("cookies");
            String token="";

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            System.out.println("token = " + token);

            if (token != null) {
                if (jwtUtil.isExpired(token)) {
                    throw new IllegalArgumentException("JWT Token is expired");
                }
            } else {
                throw new IllegalArgumentException("Missing or invalid JWT token");
            }
        }
        return message;
    }
}
