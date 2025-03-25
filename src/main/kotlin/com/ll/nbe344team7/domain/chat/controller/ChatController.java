package com.ll.nbe344team7.domain.chat.controller;

import com.ll.nbe344team7.domain.chat.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.service.ChatService;
import com.ll.nbe344team7.domain.chatroom.entity.ChatRoom;
import com.ll.nbe344team7.domain.chatroom.service.ChatroomService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/chat/rooms")
public class ChatController {

    private final ChatService chatService;
    private final ChatroomService chatroomService;

    public ChatController(ChatService chatService, ChatroomService chatroomService) {
        this.chatService = chatService;
        this.chatroomService = chatroomService;
    }

    /**
     * 메세지 보내기 컨트롤러
     *
     * @param messageDTO
     * @param roomId
     * @return

     *
     * @author jyson
     * @since 25. 3. 25.
     * */
    @PostMapping("/{roomId}")
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageDTO messageDTO,
            @PathVariable long roomId
    ) {
        Optional<ChatRoom> chatRoom = chatroomService.getItem(roomId);

        if (chatRoom.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "채팅방을 찾을 수 없습니다."
            ));

        chatService.send(messageDTO, chatRoom.get());

        return ResponseEntity.ok(Map.of(
                "message", "메세지 전송완료"
        ));
    }

    /**
     * 채팅방 입장 시 채팅 조회
     *
     * @param roomId
     * @param page
     * @param size
     * @param message
     * @return

     *
     * @author jyson
     * @since 25. 3. 25.
     * */
    @GetMapping("/{roomId}")
    public ResponseEntity<?> enterRoom(
            @PathVariable long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String message
    ) {
        Optional<ChatRoom> chatRoom = chatroomService.getItem(roomId);

        if (chatRoom.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "채팅방을 찾을 수 없습니다."
            ));

        Page<ChatMessageDTO> items = chatService.items(chatRoom.get(), message, page, size);

        return ResponseEntity.ok(items);
    }
}
