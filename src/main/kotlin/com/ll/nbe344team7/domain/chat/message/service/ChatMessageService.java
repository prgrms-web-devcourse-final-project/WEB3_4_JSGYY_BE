package com.ll.nbe344team7.domain.chat.message.service;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageSearchDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.message.repository.ChatMessageRepository;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomService chatroomService;
    private final ChatMessageSenderService chatMessageSenderService;
    private final MemberRepository memberRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatroomService chatroomService, ChatMessageSenderService chatMessageSenderService, MemberRepository memberRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatroomService = chatroomService;
        this.chatMessageSenderService = chatMessageSenderService;
        this.memberRepository = memberRepository;
    }

    /**
     * 채팅 보내기
     *
     * @param dto
     * @param member

     *
     * @author jyson
     * @since 25. 3. 25.
     */
    public void send(MessageDTO dto, Member member) {
        ChatRoom chatRoom = chatroomService.getChatRoom(dto.getRoomId());
        chatMessageSenderService.sendMessage(dto, member, chatRoom);
    }

    /**
     * 채팅방 메세지 조회
     *
     * @param roomId
     * @param dto
     * @return

     *
     * @author jyson
     * @since 25. 3. 25.
     * */
    public Page<ChatMessageDTO> getChatMessages(long roomId, MessageSearchDTO dto) {

        chatroomService.getChatRoom(roomId);
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

        if (!dto.getMessage().isEmpty()) return chatMessageRepository.findByChatRoomIdAndContentContaining(pageable, roomId, dto.getMessage()).map(ChatMessageDTO::new);
        return chatMessageRepository.findByChatRoomId(pageable, roomId).map(ChatMessageDTO::new);
    }

    /**
     * 채팅방 구독 시 메세지 읽음 처리
     *
     * @param roomId
     * @param memberId

     *
     * @author jyson
     * @since 25. 4. 6.
     * */
    @Transactional
    public void updateRead(Long roomId, Long memberId) {
        chatMessageRepository.updateRead(roomId, memberId);
    }

    public Optional<ChatMessage> findFirstByOrderByIdDesc() {
        return chatMessageRepository.findFirstByOrderByIdDesc();
    }
}