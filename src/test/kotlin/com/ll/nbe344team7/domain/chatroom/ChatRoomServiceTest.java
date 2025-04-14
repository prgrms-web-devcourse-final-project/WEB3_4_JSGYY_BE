package com.ll.nbe344team7.domain.chatroom;

import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chat.room.dto.CreateResponseDto;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.repository.ChatRoomRepository;
import com.ll.nbe344team7.domain.chat.room.service.ChatRoomRedisService;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.ChatRoomException;
import com.ll.nbe344team7.global.exception.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChatRoomServiceTest {

    @Autowired
    private ChatroomService chatRoomService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatRoomRedisService chatRoomRedisService;

    @Test
    @DisplayName("채팅방 생성")
    public void t1() {
        Optional<Member> user = memberRepository.findById(1L);
        Optional<Member> seller = memberRepository.findById(2L);

        ChatRoomRequestDto requestDto = new ChatRoomRequestDto(seller.get().getId());
        CreateResponseDto responseDto =  chatRoomService.createRoom(requestDto, user.get().getId());

        assertNotNull(responseDto);
        assertEquals("채팅방 생성 성공", responseDto.getMessage());
    }

    @Test
    @DisplayName("채팅방 생성 실패 - 상대방 유저 조회 실패")
    public void t2() {
        Optional<Member> user = memberRepository.findById(1L);
        Long sellerId = 10L;

        ChatRoomRequestDto requestDto = new ChatRoomRequestDto(sellerId);

        assertThrows(GlobalException.class, () -> {
            chatRoomService.createRoom(requestDto, user.get().getId());
        });
    }

    @Test
    @DisplayName("채팅방 목록 조회 성공")
    public void t3() {
        Optional<Member> user = memberRepository.findById(1L);

        List<ChatRoomListResponseDto> list = chatRoomRedisService.getChatRooms(user.get().getId());

        assertNotNull(list);
        assertFalse(list.isEmpty()); // 목록이 비어있지 않음을 기대
        for (ChatRoomListResponseDto dto : list) {
            assertNotNull(dto.getId());
            assertNotNull(dto.getTitle());
        }
    }

    @Test
    @DisplayName("채팅방 목록 조회 실패 - 채팅방 목록이 업는 경우")
    public void t4() {
        Optional<Member> user = memberRepository.findById(3L);

        assertThrows(ChatRoomException.class, () -> {
            chatRoomRedisService.getChatRooms(user.get().getId());
        });
    }

    @Test
    @DisplayName("채팅방 삭제 성공")
    public void t5() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoomRepository.save(chatRoom);

        chatRoomService.deleteChatroom(chatRoom.getId());

        Optional<ChatRoom> deleted = chatRoomRepository.findById(chatRoom.getId());
        assertTrue(deleted.isEmpty(), "채팅방이 삭제되어야 합니다.");
    }

    @Test
    @DisplayName("채팅방 삭제 성공 - 실패")
    public void t6() {
        Long chatRoomId = 7L;

        assertThrows(ChatRoomException.class, () -> {
            chatRoomService.deleteChatroom(chatRoomId);
        });
    }
}
