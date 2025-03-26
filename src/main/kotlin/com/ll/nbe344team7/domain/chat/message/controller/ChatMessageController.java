package com.ll.nbe344team7.domain.chat.message.controller;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/chat/rooms/{roomId}")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate template;

    public ChatMessageController(ChatMessageService chatMessageService, SimpMessagingTemplate template) {
        this.chatMessageService = chatMessageService;
        this.template = template;
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

    @MessageMapping("/messages")
    public ResponseEntity<?> send(
            @PathVariable long roomId,
            @RequestBody MessageDTO messageDTO
    ) {
        template.convertAndSend("/sub/message", messageDTO.getContent());
        chatMessageService.send(messageDTO, roomId);

        return ResponseEntity.ok(Map.of(
                "message", "메세지 전송완료"
        ));
    }
}
