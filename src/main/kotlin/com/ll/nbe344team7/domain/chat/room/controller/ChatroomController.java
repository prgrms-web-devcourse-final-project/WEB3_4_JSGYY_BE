package com.ll.nbe344team7.domain.chat.room.controller;


import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chat.room.service.ChatRoomRedisService;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 채팅방 컨트롤러
 *
 * @author kjm72
 * @since 2025-03-31
 */
@RestController
@RequestMapping("/api/chat/rooms")
@Tag(name = "채팅방 API")
public class ChatroomController {

    private final ChatroomService chatroomService;
    private final ChatRoomRedisService chatRoomRedisService;
    public ChatroomController(ChatroomService chatroomService, ChatRoomRedisService chatRoomRedisService) {
        this.chatroomService = chatroomService;
        this.chatRoomRedisService = chatRoomRedisService;
    }

    @Operation(summary = "채팅방 생성")
    @PostMapping
    public ResponseEntity<?> createChatroom(@RequestBody ChatRoomRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(chatroomService.createRoom(requestDto,userDetails.getMemberId()));
    }

    @Operation(summary = "채팅방 조회")
    @GetMapping("/user")
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomListResponseDto> response = chatRoomRedisService.getChatRooms(userDetails.getMemberId());
        return ResponseEntity.ok(Map.of("rooms", response));
    }

    @Operation(summary = "채팅방 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChatroom(@PathVariable("id") Long roomId) {
        chatroomService.deleteChatroom(roomId);
        return ResponseEntity.ok(Map.of("message","채팅방 삭제 성공"));
    }
}
