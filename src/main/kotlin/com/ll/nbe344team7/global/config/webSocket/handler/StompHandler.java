package com.ll.nbe344team7.global.config.webSocket.handler;

import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.dto.CustomUserData;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;

    public StompHandler(JWTUtil jwtUtil, RedisRepository redisRepository) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) { // WebSocket 연결 요청 시

            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            Cookie[] cookies = (Cookie[]) sessionAttributes.get("cookies");
            String token = "";

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refresh".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            String username = jwtUtil.getUsername(token);
            Long memberId = jwtUtil.getMemberId(token);
            String role = jwtUtil.getRole(token);

            CustomUserData customUserData = new CustomUserData(memberId, username, role, "tmp");
            CustomUserDetails customUserDetails = new CustomUserDetails(customUserData);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            accessor.setUser(authToken);

            if (token != null) {
                if (jwtUtil.isExpired(token)) {
                    throw new IllegalArgumentException("JWT Token is expired");
                }
            } else {
                throw new IllegalArgumentException("Missing or invalid JWT token");
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            String userId = "";
            Principal principal = accessor.getUser();

            if (principal != null) {
                if (principal instanceof UsernamePasswordAuthenticationToken auth) {
                    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
                    userId = String.valueOf(user.getMemberId());
                }

                if (destination != null && !destination.isEmpty()) {
                    String[] path = destination.split("/");
                    String roomId = path[4];

                    redisRepository.saveChatroom("chatroom:" + roomId + ":users", userId);
                    redisRepository.saveSubscription("subscription:room", userId, roomId);
                }
            }
        } else if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {

            String userId = "";
            Principal principal = accessor.getUser();

            if (principal != null) {
                if (principal instanceof UsernamePasswordAuthenticationToken auth) {
                    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
                    userId = String.valueOf(user.getMemberId());
                }

                String roomId = redisRepository.getRoomId("subscription:room", userId);

                if (roomId != null) {
                    redisRepository.deleteChatroom("chatroom:" + roomId + ":users", userId);
                    redisRepository.deleteSubscription("subscription:room", userId);
                }
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {

            accessor.setUser(null);
        }
        return message;
    }
}
