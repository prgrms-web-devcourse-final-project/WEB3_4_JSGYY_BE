package com.ll.nbe344team7.global.config.webSocket.handler;

import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.repository.ChatRoomRedisRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.ll.nbe344team7.global.redis.RedisRepository;
import com.ll.nbe344team7.global.security.dto.CustomUserData;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import com.ll.nbe344team7.global.security.exception.SecurityException;
import com.ll.nbe344team7.global.security.exception.SecurityExceptionCode;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Component
public class StompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;
    private final ChatRoomRedisRepository chatRoomRedisRepository;

    public StompHandler(JWTUtil jwtUtil, RedisRepository redisRepository, ChatRoomRedisRepository chatRoomRedisRepository) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
        this.chatRoomRedisRepository = chatRoomRedisRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (accessor.getCommand()) {
            case CONNECT ->  handleConnect(accessor);
            case SUBSCRIBE -> handleSubscribe(accessor);
            case UNSUBSCRIBE -> handleUnsubscribe(accessor);
            case DISCONNECT -> handleDisconnect(accessor);
        }
        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
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
                throw new SecurityException(SecurityExceptionCode.REFRESHTOKEN_IS_EXPIRED);
            }
        } else {
            throw new SecurityException(SecurityExceptionCode.NOT_FOUND_REFRESHTOKEN);
        }
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Principal principal = accessor.getUser();

        if (principal != null) {
            String userId = getUser(principal);
            Long memberId = Long.valueOf(userId);

            if (destination != null && !destination.isEmpty()) {
                if (destination.startsWith("/sub/chat/room/")) {
                    // websocket 채팅방 구독
                    String[] path = destination.split("/");
                    String roomIdStr = path[4];
                    Long roomId = Long.valueOf(roomIdStr);

                    List<ChatRoomListResponseDto> chatRoomList = chatRoomRedisRepository.getChatRoomList(memberId);
                    if (chatRoomList != null) {
                        for (int i = 0; i < chatRoomList.size(); i++) {
                            ChatRoomListResponseDto dto = chatRoomList.get(i);
                            if (dto.getId() == roomId) {
                                dto.setUnReadCount(0);
                                chatRoomList.set(i, dto);
                                break;
                            }
                        }
                        chatRoomRedisRepository.saveChatRoomList(memberId, chatRoomList); // 📦 다시 저장
                    }

                    redisRepository.saveChatroom("chatroom:" + roomId + ":users", userId);
                    redisRepository.saveSubscription("subscription:room", userId, roomIdStr);
                }
            }
        } else {
            throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
        }
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();

        if (principal != null) {
            String userId = getUser(principal);

            String roomId = redisRepository.getRoomId("subscription:room", userId);

            if (!roomId.isBlank()) {
                redisRepository.deleteChatroom("chatroom:" + roomId + ":users", userId);
                redisRepository.deleteSubscription("subscription:room", userId);
            }
        }
        else {
            throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
        }
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        accessor.getSessionAttributes().clear();
    }

    private String getUser(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken auth) {
            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
            return String.valueOf(user.getMemberId());
        }
        throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
    }
}
