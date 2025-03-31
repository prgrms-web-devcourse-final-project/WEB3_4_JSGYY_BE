package com.ll.nbe344team7.domain.chat.room.controller;


import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
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
public class ChatroomController {

    private final ChatroomService chatroomService;
    public ChatroomController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    @PostMapping
    public ResponseEntity<?> createChatroom(@RequestBody ChatRoomRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(chatroomService.createRoom(requestDto,userDetails.getMemberId()));
    }


    @GetMapping("/user")
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomListResponseDto> response = chatroomService.listChatRoom(userDetails.getMemberId());
        return ResponseEntity.ok(Map.of("rooms", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChatroom(@PathVariable("id") Long roomId) {
        chatroomService.deleteChatroom(roomId);
        return ResponseEntity.ok(Map.of("message","채팅방 삭제 성공"));
    }
}
