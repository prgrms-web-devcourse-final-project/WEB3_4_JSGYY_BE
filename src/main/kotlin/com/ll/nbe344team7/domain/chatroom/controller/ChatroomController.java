package com.ll.nbe344team7.domain.chatroom.controller;

import com.ll.nbe344team7.domain.chatroom.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chatroom.service.ChatroomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if(requestDto.getSellerId() == 10000L || requestDto.getUserId() == 10000L){
            return ResponseEntity.status(404).body(Map.of("message","해당 유저를 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(this.chatroomService.createRoom(requestDto.getPostId(), requestDto.getSellerId(), requestDto.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChatrooms(@PathVariable("id") Long userId) {
        if(userId == 10000L){
            return ResponseEntity.status(404).body(Map.of("message","채팅목록을 찾을 수 없습니다."));
        }
        return ResponseEntity.ok(this.chatroomService.listChatrooms(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChatroom(@PathVariable("id") Long roomId) {
        if(roomId == 10000L){
            return ResponseEntity.status(404).body(Map.of("message","해당 채팅방이 존재하지 않습니다."));
        }
        return ResponseEntity.ok(this.chatroomService.deleteChatroom(roomId));
    }
}
