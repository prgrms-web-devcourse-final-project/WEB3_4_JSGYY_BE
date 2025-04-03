package com.ll.nbe344team7.domain.chat.participant.service;

import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.repository.ChatParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatParticipantService {
    private final ChatParticipantRepository chatParticipantRepository;

    public ChatParticipantService(ChatParticipantRepository chatParticipantRepository) {
        this.chatParticipantRepository = chatParticipantRepository;
    }

    public List<ChatParticipant> getChatParticipants(Long chatroomId) {
        return chatParticipantRepository.findByChatroomId(chatroomId);
    }
}
