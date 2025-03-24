package com.ll.nbe344team7.domain.chat.service;

import com.ll.nbe344team7.domain.chat.dto.ChatMessageDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class ChatService {

    public Map<Object, Object> send(ChatMessageDTO dto) {


        return Map.of("message", "전송성공");
    }

    public Map<Object, Object> items(long roomId, String message) {

        List<ChatMessageDTO> list = new ArrayList<>();

        if (!message.isEmpty()) {
            list.add(new ChatMessageDTO(1, 1, 1, "안녕하세요", LocalDateTime.now()));
            list.add(new ChatMessageDTO(2, 2, 1, "안녕하세요", LocalDateTime.now()));
        } else {
            list.add(new ChatMessageDTO(1, 1, 1, "안녕하세요", LocalDateTime.now()));
            list.add(new ChatMessageDTO(2, 2, 1, "안녕하세요", LocalDateTime.now()));
            list.add(new ChatMessageDTO(3, 2, 1, "무슨일이세요?", LocalDateTime.now()));
            list.add(new ChatMessageDTO(4, 1, 1, "궁금합니다.", LocalDateTime.now()));
        }

        return Map.of("messages", list);
    }
}
