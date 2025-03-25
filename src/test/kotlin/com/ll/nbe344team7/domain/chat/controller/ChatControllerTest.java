package com.ll.nbe344team7.domain.chat.controller;

import com.ll.nbe344team7.domain.chat.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class ChatControllerTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("메세지 전송")
    void t1() throws Exception {
        mvc.perform(
                        post("/api/chat/rooms/1")
                                .content(
                                        """
                                                {
                                                    "content" : "안녕하세요"
                                                }
                                                """
                                )
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print())
                .andExpect(handler().handlerType(ChatController.class))
                .andExpect(handler().methodName("sendMessage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("메세지 전송완료"));
    }

    @Test
    @DisplayName("메세지 전송, 없는 채팅방")
    void t2() throws Exception {
        mvc.perform(
                        post("/api/chat/rooms/2")
                                .content(
                                        """
                                                {
                                                    "content" : "안녕하세요"
                                                }
                                                """
                                )
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print())
                .andExpect(handler().handlerType(ChatController.class))
                .andExpect(handler().methodName("sendMessage"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("채팅방이 조회되지 않습니다."));    }

    @Test
    @DisplayName("메세지 조회")
    void t3() throws Exception {
        mvc.perform(
                        get("/api/chat/rooms/1")
                )
                .andDo(print())
                .andExpect(handler().handlerType(ChatController.class))
                .andExpect(handler().methodName("enterRoom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("메세지 조회, 없는 채팅방")
    void t4() throws Exception {
        mvc.perform(
                        get("/api/chat/rooms/2")
                )
                .andDo(print())
                .andExpect(handler().handlerType(ChatController.class))
                .andExpect(handler().methodName("enterRoom"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("채팅방이 조회되지 않습니다."));
    }
}
