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


    @GetMapping("/user/{id}")
    public ResponseEntity<?> getChatRooms(@PathVariable Long id) {
        List<ChatRoomListResponseDto> response = chatroomService.listChatRoom(id);
        return ResponseEntity.ok(Map.of("rooms", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChatroom(@PathVariable("id") Long roomId) {
        chatroomService.deleteChatroom(roomId);
        return ResponseEntity.ok(Map.of("message","채팅방 삭제 성공"));
    }
}
