package com.ll.nbe344team7.global.config.webSocket.handler.command;

import com.ll.nbe344team7.domain.chat.redis.repository.ChatRedisRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * 웹 소캣 구독 취소 명령
 *
 * @author jyson
 * @since 25. 4. 7.
 */
@Component
public class UnSubscribeHandler implements StompCommandHandler{

    private final ChatRedisRepository chatRedisRepository;

    public UnSubscribeHandler(ChatRedisRepository chatRedisRepository) {
        this.chatRedisRepository = chatRedisRepository;
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();

        if (principal != null) {
            String userId = getUser(principal);

            String roomId = chatRedisRepository.getRoomId("subscription:room", userId);

            if (!roomId.isBlank()) {
                chatRedisRepository.deleteChatroom("chatroom:" + roomId + ":users", userId);
                chatRedisRepository.deleteSubscription("subscription:room", userId);
            }
        }
        else {
            throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
        }
    }

    private String getUser(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken auth) {
            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
            return String.valueOf(user.getMemberId());
        }
        throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
    }
}
