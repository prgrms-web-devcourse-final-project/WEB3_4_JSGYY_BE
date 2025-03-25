package com.ll.nbe344team7.domain.chat.service;

import com.ll.nbe344team7.domain.chat.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.exception.ChatException;
import com.ll.nbe344team7.domain.chat.repository.ChatRepository;
import com.ll.nbe344team7.domain.chatroom.entity.ChatRoom;
import com.ll.nbe344team7.domain.chatroom.service.ChatroomService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.chat.exception.ChatExceptionCode;
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
    private final ChatroomService chatroomService;

    public ChatService(ChatRepository chatRepository, ChatroomService chatroomService) {
        this.chatRepository = chatRepository;
        this.chatroomService = chatroomService;
    }

    /**
     * 채팅 보내기
     *
     * 1. 채팅방 있는지 확인
     * 2. 채팅방에 자신이 포함되어 있는지 확인해야 될까?????
     *
     * @param dto
     * @param roomId

     *
     * @author jyson
     * @since 25. 3. 25.
     */
    public void send(MessageDTO dto, long roomId) {
        ChatRoom chatRoom = chatroomService.getItem(roomId).orElseThrow(
                () -> new ChatException(ChatExceptionCode.NOT_FOUND_ROOM)
        );

        ChatMessage chatMessage = new ChatMessage(new Member(), dto.getContent(), chatRoom);

        chatRepository.save(chatMessage);
    }

    /**
     * 채팅방 메세지 조회
     *
     * @param roomId
     * @param message
     * @param page
     * @param size
     * @return

     *
     * @author jyson
     * @since 25. 3. 25.
     * */
    public Page<ChatMessageDTO> items(long roomId, String message, int page, int size) {
        ChatRoom chatRoom = chatroomService.getItem(roomId).orElseThrow(
                () -> new ChatException(ChatExceptionCode.NOT_FOUND_ROOM)
        );
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (!message.isEmpty()) return chatRepository.findByChatRoomIdAndContentContaining(pageable, chatRoom.getId(), message).map(ChatMessageDTO::new);
        return chatRepository.findByChatRoomId(pageable, chatRoom.getId()).map(ChatMessageDTO::new);
    }
}
