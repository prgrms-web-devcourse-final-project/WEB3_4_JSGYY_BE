package com.ll.nbe344team7.domain.chatroom.service;

import com.ll.nbe344team7.domain.chatroom.dto.ChatRoomRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author kjm72
 * @since 2025-03-24
 */

@Service
public class ChatroomService {

    public Map<Object, Object> createRoom(ChatRoomRequestDto chatRoomRequestDto) {
        return Map.of("message","채팅방 생성 성공");
    }

    public Map<Object,Object> listChatrooms(Long memberId){
        List<Map<Object, Object>> rooms = List.of(
                Map.of("id", 1, "nickname", "시계매니아"),
                Map.of("id", 2, "nickname", "모아신발")
        );

        return Map.of("rooms",rooms);
    }

    public Map<Object,Object> deleteChatroom(Long roomId) {
        return Map.of("message","채팅방 삭제 성공");
    }
}
