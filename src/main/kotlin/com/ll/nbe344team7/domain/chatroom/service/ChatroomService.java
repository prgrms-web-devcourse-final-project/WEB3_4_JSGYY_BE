package com.ll.nbe344team7.domain.chatroom.service;

import com.ll.nbe344team7.domain.chatparticipant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chatparticipant.repository.ChatParticipantRepository;
import com.ll.nbe344team7.domain.chatroom.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chatroom.dto.CreateResponseDto;
import com.ll.nbe344team7.domain.chatroom.entity.ChatRoom;
import com.ll.nbe344team7.domain.chatroom.repository.ChatRoomRepository;
import com.ll.nbe344team7.domain.member.Member;
import com.ll.nbe344team7.domain.member.MemberRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 *
 *
 * @author kjm72
 * @since 2025-03-24
 */

@Service
public class ChatroomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public ChatroomService(ChatRoomRepository chatRoomRepository, MemberRepository memberRepository, ChatParticipantRepository chatParticipantRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.chatParticipantRepository = chatParticipantRepository;
    }

    @Transactional
    public CreateResponseDto createRoom(ChatRoomRequestDto requestDto) {
        Member seller = memberRepository.findById(requestDto.getSellerId()).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Member user = memberRepository.findById(requestDto.getUserId()).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        ChatRoom chatroom = new ChatRoom();
        ChatParticipant userChatParticipant = new ChatParticipant(chatroom, user);
        ChatParticipant sellerChatParticipant = new ChatParticipant(chatroom, seller);
        chatroom.addParticipant(userChatParticipant);
        chatroom.addParticipant(sellerChatParticipant);

        chatroom.setTitle(user.getId());
        chatRoomRepository.save(chatroom);

        return new CreateResponseDto("채팅방 생성 성공");
    }



    public Map<Object,Object> deleteChatroom(Long roomId) {
        return Map.of("message","채팅방 삭제 성공");
    }
}
