package com.ll.nbe344team7.global.imageFile;

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO;
import com.ll.nbe344team7.domain.alarm.entity.Alarm;
import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.global.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;
    private BlockingQueue<Object> messageQueue;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ChatroomService chatroomService;

    @BeforeEach
    void setup() {
        messageQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    @DisplayName("웹 소캣 레디스 채널 chatroom 발행")
    void t1() throws Exception {
        String refreshToken = jwtUtil.createJwt("refresh", "admin1", 1L, "admin1", "admin", 1);

        Cookie cookie = new Cookie("refresh", refreshToken);
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Cookie", cookie.getName() + "=" + cookie.getValue());

        // 1. WebSocket 연결
        StompSession session = stompClient.connectAsync(
                "ws://localhost:" + port + "/ws", headers,
                new StompSessionHandlerAdapter() {}
        ).get(5, TimeUnit.SECONDS);

        // 2. 메시지 구독
        session.subscribe("/sub/chat/room/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class; // [!code focus]
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageQueue.add((ChatMessageDTO) payload); // [!code focus]
            }
        });

        // 3. Redis에 메시지 발행
        MessageDTO messageDTO = new MessageDTO("안녕", 1L);
        ChatRoom chatRoom = chatroomService.getChatRoom(messageDTO.getRoomId());
        ChatMessage chatMessage = new ChatMessage(new Member(1L, "admin1"), messageDTO.getContent(), chatRoom);
        redisTemplate.convertAndSend("chatroom", new ChatMessageDTO(chatMessage));

        // 4. WebSocket 응답 검증
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            ChatMessageDTO receivedMessage = (ChatMessageDTO) messageQueue.poll();
            assertThat(receivedMessage).isEqualTo(new ChatMessageDTO(chatMessage));
        });

        session.disconnect();
    }

    @Test
    @DisplayName("웹 소캣 레디스 채널 notification 발행")
    void t2() throws Exception {
        String refreshToken = jwtUtil.createJwt("refresh", "admin1", 1L, "admin1", "admin", 1);

        Cookie cookie = new Cookie("refresh", refreshToken);
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Cookie", cookie.getName() + "=" + cookie.getValue());

        // 1. WebSocket 연결
        StompSession session = stompClient.connectAsync(
                "ws://localhost:" + port + "/ws", headers,
                new StompSessionHandlerAdapter() {}
        ).get(5, TimeUnit.SECONDS);

        // 2. 메시지 구독
        session.subscribe("/sub/notifications/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return AlarmDTO.class; // [!code focus]
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageQueue.add((AlarmDTO) payload); // [!code focus]
            }
        });

        // 3. Redis에 메시지 발행
        Alarm alarm = new Alarm(new Member(2L, "admin1"), "admin1: 안녕", 2, 1L);
        redisTemplate.convertAndSend("notification", new AlarmDTO(alarm));

        // 4. WebSocket 응답 검증
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            AlarmDTO receivedMessage = (AlarmDTO) messageQueue.poll();
            assertThat(receivedMessage).isEqualTo(new AlarmDTO(alarm));
        });

        session.disconnect();
    }
}
