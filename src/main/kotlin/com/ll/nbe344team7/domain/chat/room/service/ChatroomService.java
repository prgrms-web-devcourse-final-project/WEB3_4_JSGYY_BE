package com.ll.nbe344team7.domain.chat.room.service;

import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.repository.ChatParticipantRepository;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chat.room.dto.CreateResponseDto;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.repository.ChatRoomRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.ChatRoomException;
import com.ll.nbe344team7.global.exception.ChatRoomExceptionCode;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 채팅룸 생성
     *
     * @param requestDto
     * @return
     *
     * @author kjm72
     * @since 2025-03-26
     */
    @Transactional
    public CreateResponseDto createRoom(ChatRoomRequestDto requestDto,Long memberId) {
        Member seller = memberRepository.findById(requestDto.getUserId()).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        Member user = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        ChatRoom chatroom = new ChatRoom();
        ChatParticipant userChatParticipant = new ChatParticipant(chatroom, user);
        ChatParticipant sellerChatParticipant = new ChatParticipant(chatroom, seller);
        chatroom.addParticipant(userChatParticipant);
        chatroom.addParticipant(sellerChatParticipant);

        chatroom.setTitle(user.getId());
        chatRoomRepository.save(chatroom);

        return new CreateResponseDto("채팅방 생성 성공");
    }

    /**
     * 채팅방 목록 조회
     *
     * @param id
     * @return
     *
     * @author kjm72
     * @since 2025-03-26
     */
    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDto> listChatRoom(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));
        List<ChatRoomListResponseDto> list = chatParticipantRepository.findByMemberId(member.getId()).stream()
                .map(cp ->{
                    ChatRoom chatroom = cp.getChatroom();
                    return new ChatRoomListResponseDto(
                            chatroom.getId(),
                            chatroom.getTitle(),
                            ""
                    );
                })
                .toList();
        if (list.isEmpty()) {
            throw new ChatRoomException(ChatRoomExceptionCode.NOT_FOUND_LIST);
        }

        return list;
    }

    /**
     * 채팅방 삭제
     *
     * @param roomId
     * @return
     *
     * @author kjm72
     * @since 2025-03-26
     */
    public void deleteChatroom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatRoomException(ChatRoomExceptionCode.NOT_FOUND_ROOM));
        chatRoomRepository.delete(chatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoom getChatRoom(long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatRoomException(ChatRoomExceptionCode.NOT_FOUND_ROOM));
        return chatRoom;
    }
}
