package com.ll.nbe344team7.domain.chat.service;

import com.ll.nbe344team7.domain.chat.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.repository.ChatRepository;
import com.ll.nbe344team7.domain.chatroom.entity.ChatRoom;
import com.ll.nbe344team7.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    /**
     * 채팅 보내기
     *
     * 1. 채팅방 있는지 확인
     * 2. 채팅방에 자신이 포함되어 있는지 확인해야 될까?????
     *
     * @param dto
     * @param chatRoom

     *
     * @author jyson
     * @since 25. 3. 25.
     */
    public void send(MessageDTO dto, ChatRoom chatRoom) {
        ChatMessage chatMessage = new ChatMessage(new Member(), dto.getContent(), chatRoom);

        chatRepository.save(chatMessage);
    }

    /**
     * 채팅방 메세지 조회
     *
     * @param chatRoom
     * @param message
     * @param page
     * @param size
     * @return

     *
     * @author jyson
     * @since 25. 3. 25.
     * */
    public Page<ChatMessageDTO> items(ChatRoom chatRoom, String message, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (!message.isEmpty()) return chatRepository.findByChatRoomIdAndContentContaining(pageable, chatRoom.getId(), message).map(ChatMessageDTO::new);
        return chatRepository.findByChatRoomId(pageable, chatRoom.getId()).map(ChatMessageDTO::new);
    }
}
