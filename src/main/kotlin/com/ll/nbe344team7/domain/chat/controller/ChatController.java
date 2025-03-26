package com.ll.nbe344team7.domain.chat.controller;

import com.ll.nbe344team7.domain.chat.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/chat/rooms")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
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
        chatService.send(messageDTO, roomId);

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
        Page<ChatMessageDTO> chats = chatService.getChatMessages(roomId, message, page, size);

        return ResponseEntity.ok(chats);
    }
}
