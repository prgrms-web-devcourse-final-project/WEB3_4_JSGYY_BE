package com.ll.nbe344team7.domain.chat.message.controller;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
     * 메세지 보내기 컨트롤러
     *
     * @param messageDTO
     * @param roomId
     * @return
     * @author jyson
     * @since 25. 3. 25.
     */
    @Operation(summary = "메시지 전송")
    @PostMapping
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageDTO messageDTO,
            @PathVariable long roomId
    ) {
        chatMessageService.send(messageDTO, roomId);

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
     * @author jyson
     * @since 25. 3. 25.
     */
    @Operation(summary = "채팅방 입장 시 채팅 조회")
    @GetMapping
    public ResponseEntity<?> enterRoom(
            @PathVariable long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String message
    ) {
        Page<ChatMessageDTO> chats = chatMessageService.getChatMessages(roomId, message, page, size);

        return ResponseEntity.ok(chats);
    }
}
