package com.ll.nbe344team7.domain.chat.room.service;

import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.repository.ChatParticipantRepository;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomCreateResponseDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.repository.ChatRoomRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ChatRoomCreateResponseDto createRoom(ChatRoomRequestDto requestDto) {
        Member seller = memberRepository.findById(requestDto.getSellerId()).orElseThrow(()-> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Member user = memberRepository.findById(requestDto.getUserId()).orElseThrow(()-> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        ChatRoom chatRoom = ChatRoom.Companion.create();
        chatParticipantRepository.saveAll(List.of(
                ChatParticipant.Companion.create(chatRoom, seller),
                ChatParticipant.Companion.create(chatRoom, user)
        ));

        return ChatRoomCreateResponseDto.Companion.success();
    }



    public Map<Object,Object> deleteChatroom(Long roomId) {
        return Map.of("message","채팅방 삭제 성공");
    }

    public Optional<ChatRoom> getChatRoom(long roomId) {
        return chatRoomRepository.findById(roomId);
    }
}
