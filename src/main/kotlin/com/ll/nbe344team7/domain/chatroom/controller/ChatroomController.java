package com.ll.nbe344team7.domain.chatroom.controller;

import com.ll.nbe344team7.domain.chatroom.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chatroom.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chatroom.service.ChatroomService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createChatroom(@RequestBody ChatRoomRequestDto requestDto) {
        return ResponseEntity.ok(chatroomService.createRoom(requestDto));
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
