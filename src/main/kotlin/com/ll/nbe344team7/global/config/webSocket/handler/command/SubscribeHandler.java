package com.ll.nbe344team7.global.config.webSocket.handler.command;

import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import com.ll.nbe344team7.domain.chat.redis.repository.ChatRedisRepository;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

/**
 * 웹 소캣 구독 명령
 *
 * @author jyson
 * @since 25. 4. 7.
 */
@Component
public class SubscribeHandler implements StompCommandHandler{

    private final ChatRedisRepository chatRedisRepository;
    private final ChatMessageService chatMessageService;

    public SubscribeHandler(ChatRedisRepository chatRedisRepository, ChatMessageService chatMessageService) {
        this.chatRedisRepository = chatRedisRepository;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public void handle(StompHeaderAccessor accessor) {
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
                    Long roomId = Long.parseLong(roomIdStr);
                    chatMessageService.updateRead(roomId, memberId);

                    List<ChatRoomListResponseDto> chatRoomList = chatRedisRepository.getChatRoomList(memberId);
                    if (chatRoomList != null) {
                        for (int i = 0; i < chatRoomList.size(); i++) {
                            ChatRoomListResponseDto dto = chatRoomList.get(i);
                            if (dto.getId() == roomId) {
                                dto.setUnReadCount(0);
                                chatRoomList.set(i, dto);
                                break;
                            }
                        }
                        chatRedisRepository.saveChatRoomList(memberId, chatRoomList); // 📦 다시 저장
                    }

                    chatRedisRepository.saveChatroom("chatroom:" + roomId + ":users", userId);
                    chatRedisRepository.saveSubscription("subscription:room", userId, roomIdStr);
                }
            }
        } else {
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
