package com.ll.nbe344team7.domain.chat.controller;

import com.ll.nbe344team7.domain.chat.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.service.ChatService;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{roomId}")
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageDTO messageDTO,
            @PathVariable long roomId
    ) {
        if (roomId == 10000)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "채팅방을 찾을 수 없습니다."));

        return ResponseEntity.ok(chatService.send(messageDTO));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> enterRoom(
            @PathVariable long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String message
    ) {
        if (roomId == 10000)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "채팅방을 찾을 수 없습니다."));

        return ResponseEntity.ok(chatService.items(roomId, message));
    }
}
