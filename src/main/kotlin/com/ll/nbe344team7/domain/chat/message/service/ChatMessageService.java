package com.ll.nbe344team7.domain.chat.message.service;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.repository.ChatMessageRepository;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomService chatroomService;
    private final ChatMessageSender chatMessageSender;
    private final MemberRepository memberRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatroomService chatroomService, ChatMessageSender chatMessageSender, MemberRepository memberRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatroomService = chatroomService;
        this.chatMessageSender = chatMessageSender;
        this.memberRepository = memberRepository;
    }

    /**
     * 채팅 보내기
     *
     * @param dto
     * @param memberId

     *
     * @author jyson
     * @since 25. 3. 25.
     */
    @Transactional
    public void send(MessageDTO dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        ChatRoom chatRoom = chatroomService.getChatRoom(dto.getRoomId());
        chatMessageSender.sendMessage(dto, member, chatRoom);
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
    public Page<ChatMessageDTO> getChatMessages(long roomId, String message, int page, int size) {

        chatroomService.getChatRoom(roomId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (!message.isEmpty()) return chatMessageRepository.findByChatRoomIdAndContentContaining(pageable, roomId, message).map(ChatMessageDTO::new);
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
}