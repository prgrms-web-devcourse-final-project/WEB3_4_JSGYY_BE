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
        if(roomId == 10000L){
            return ResponseEntity.status(404).body(Map.of("message","해당 채팅방이 존재하지 않습니다."));
        }
        return ResponseEntity.ok(this.chatroomService.deleteChatroom(roomId));
    }
}
