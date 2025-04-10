package com.ll.nbe344team7.domain.chat.service;

import com.ll.nbe344team7.domain.chat.message.controller.ChatMessageController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        chatMessageService.send(dto, member.getId());
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

        // then
        assertThatThrownBy(() -> chatroomService.getChatRoom(dto.getRoomId()))
                .isInstanceOf(ChatRoomException.class)
                .hasMessageContaining(ChatRoomExceptionCode.NOT_FOUND_ROOM.getMessage());
    }

    @Test
    @DisplayName("채팅방 메세지 조회")
    void t3() throws Exception {
        // given
        MessageSearchDTO dto = new MessageSearchDTO("안녕하세요", 0, 10);

        // when
        Page<ChatMessageDTO> chatMessages = chatMessageService.getChatMessages(1L, dto);

        // then
//        assertThat(saveChatMessage.getMember()).isEqualTo(member);
//        assertThat(saveChatMessage.getContent()).isEqualTo(dto.getContent());
//        assertThat(saveChatMessage.getChatRoom()).isEqualTo(chatRoom);
//        assertThat(saveChatMessage.isRead()).isEqualTo(false);
    }

    @Test
    @DisplayName("메세지 조회, 없는 채팅방")
    void t4() throws Exception {
        mvc.perform(
                        get("/api/chat/rooms/2")
                )
                .andDo(print())
                .andExpect(handler().handlerType(ChatMessageController.class))
                .andExpect(handler().methodName("enterRoom"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("채팅방이 조회되지 않습니다."));
    }
}
