package com.ll.nbe344team7.domain.chat.message.controller;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageSearchDTO;
import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/chat/rooms/{roomId}")
@Tag(name = "채팅 메시지 API")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    /**
     * 채팅방 입장 시 채팅 조회
     *
     * @param roomId
     * @param messageSearchDTO
     * @return
     * @author jyson
     * @since 25. 3. 25.
     */
    @Operation(summary = "채팅방 입장 시 채팅 조회")
    @GetMapping
    public ResponseEntity<?> enterRoom(
            @PathVariable long roomId,
            @ModelAttribute MessageSearchDTO messageSearchDTO
            ) {
        Page<ChatMessageDTO> chats = chatMessageService.getChatMessages(roomId, messageSearchDTO);

        return ResponseEntity.ok(chats);
    }
}
