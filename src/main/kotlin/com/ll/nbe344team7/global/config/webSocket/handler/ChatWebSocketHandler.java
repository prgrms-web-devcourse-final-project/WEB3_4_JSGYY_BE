package com.ll.nbe344team7.global.config.webSocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//ws -> A Session, A Stomp(subscribe)
//ws -> B Session, A Stomp(subscribe)
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    //동시성을 해결하기 위해 ConcurrentHashMap 사용
    private static final ConcurrentHashMap<Long, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long roomId = getRoomIdFromSession(session);

        chatRooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);

        System.out.println("[+] room :: " + roomId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Long roomId = getRoomIdFromSession(session);
        System.out.println("[+] Message received in room " + roomId + " :: " + message.getPayload());

        for (WebSocketSession s : chatRooms.getOrDefault(roomId, Set.of())){
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        Long roomId = getRoomIdFromSession(session);

        chatRooms.getOrDefault(roomId, Set.of()).remove(session);
        System.out.println("[+] afterConnectionClosed - Session: " + session.getId() + ", CloseStatus: " + status + ", room: " + roomId);
    }

    private Long getRoomIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        return Long.valueOf(query.split("=")[1]);
    }

}