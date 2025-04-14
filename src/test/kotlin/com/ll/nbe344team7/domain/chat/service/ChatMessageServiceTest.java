package com.ll.nbe344team7.domain.chat.service;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageSearchDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.service.MemberService;
import com.ll.nbe344team7.global.exception.ChatRoomException;
import com.ll.nbe344team7.global.exception.ChatRoomExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * 채팅 컨트롤러 테스트
 *
 * @author jyson
 * @since 25. 3. 25.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChatMessageServiceTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("메세지 전송")
    void t1() {
        // given
        MessageDTO dto = new MessageDTO("안녕하세요", 1L);
        Member member = memberService.getMember(1L);
        ChatRoom chatRoom = chatroomService.getChatRoom(dto.getRoomId());

        // when
        chatMessageService.send(dto, new Member(member.getId(), member.getNickname()));
        ChatMessage saveChatMessage = chatMessageService.findFirstByOrderByIdDesc().get();

        // then
        assertThat(saveChatMessage.getMember()).isEqualTo(member);
        assertThat(saveChatMessage.getContent()).isEqualTo(dto.getContent());
        assertThat(saveChatMessage.getChatRoom()).isEqualTo(chatRoom);
        assertThat(saveChatMessage.isRead()).isEqualTo(false);
    }

    @Test
    @DisplayName("메세지 전송, 없는 채팅방")
    void t2() {
        // given
        MessageDTO dto = new MessageDTO("안녕하세요", 2L);
        Member member = memberService.getMember(1L);

        // then
        assertThatThrownBy(() -> chatMessageService.send(dto, new Member(member.getId(), member.getNickname())))
                .isInstanceOf(ChatRoomException.class)
                .hasMessageContaining(ChatRoomExceptionCode.NOT_FOUND_ROOM.getMessage());
    }

    @Test
    @DisplayName("채팅방 메세지 조회")
    void t3() {
        // given

        // when
        Page<ChatMessageDTO> chatMessages = chatMessageService.getChatMessages(1L, new MessageSearchDTO());

        // then
        assertThat(chatMessages).isNotNull();
    }

    @Test
    @DisplayName("메세지 조회, 없는 채팅방")
    void t4() {
        // then
        assertThatThrownBy(() -> chatMessageService.getChatMessages(2L, new MessageSearchDTO()))
                .isInstanceOf(ChatRoomException.class)
                .hasMessageContaining(ChatRoomExceptionCode.NOT_FOUND_ROOM.getMessage());
    }
}
