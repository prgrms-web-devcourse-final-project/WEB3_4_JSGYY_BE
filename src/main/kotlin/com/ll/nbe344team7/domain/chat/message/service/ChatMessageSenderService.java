package com.ll.nbe344team7.domain.chat.message.service;

import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.message.repository.ChatMessageRepository;
import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.service.ChatParticipantService;
import com.ll.nbe344team7.domain.chat.redis.repository.ChatRedisRepository;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.global.config.reddison.lock.DistributedLock;
import com.ll.nbe344team7.global.config.redis.publisher.ChatRedisPublisher;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 메세지 전송 처리
 *
 * @author jyson
 * @since 25. 4. 7.
 */
@Service
public class ChatMessageSenderService {

    private static final Logger log = LoggerFactory.getLogger(ChatMessageSenderService.class);

    private final ChatParticipantService chatParticipantService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRedisPublisher chatRedisPublisher;
    private final ChatRedisRepository chatRedisRepository;
    private final DistributedLock lock;


    public ChatMessageSenderService(ChatParticipantService chatParticipantService, ChatMessageRepository chatMessageRepository, ChatRedisPublisher chatRedisPublisher, ChatRedisRepository chatRedisRepository, DistributedLock lock) {
        this.chatParticipantService = chatParticipantService;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRedisPublisher = chatRedisPublisher;
        this.chatRedisRepository = chatRedisRepository;
        this.lock = lock;
    }

    @Transactional
    public void sendMessage(MessageDTO dto, Member member, ChatRoom chatRoom) {
        Long roomId = chatRoom.getId();

        try {
            lock.executeWithLock(roomId, () -> {
                // 채널 chatroom pub
                ChatMessage chatMessage = new ChatMessage(member, dto.getContent(), chatRoom);
                chatMessageRepository.save(chatMessage);
                chatRedisPublisher.publishMessage(dto, chatMessage);

                // 채널 chatroomList pub
                List<ChatParticipant> chatParticipants = chatParticipantService.getChatParticipants(roomId);
                chatRedisPublisher.publishRoomListUpdate(member.getId(), chatParticipants);

                Set<String> chatroomUsers = chatRedisRepository.getChatroomUsers("chatroom:" + roomId + ":users");

                if (chatroomUsers.size() < 2) {
                    List<ChatParticipant> offlineUsers = chatParticipants.stream()
                            .filter(p -> !chatroomUsers.contains(String.valueOf(p.getMember().getId())))
                            .toList();

                    // 메세지 안읽음
                    chatMessage.setRead(false);
                    redisUpdateReadCount(offlineUsers, roomId);

                    // 알림 전송 로직
                }
            });
        } catch (LockAcquisitionException e) {
            log.error("메세지 전송 실패 - 락 획득 실패: {}", roomId);
        }
    }

    /**
     * 안읽은 메세지 count 처리
     *
     * @param offlineUsers
     * @param roomId

     *
     * @author jyson
     * @since 25. 4. 7.
     */
    private void redisUpdateReadCount(List<ChatParticipant> offlineUsers, Long roomId) {
        List<ChatRoomListResponseDto> chatRoomList = chatRedisRepository.getChatRoomList(offlineUsers.getFirst().getId());

        for (int i = 0; i < chatRoomList.size(); i++) {
            ChatRoomListResponseDto chatRoomListResponseDto = chatRoomList.get(i);
            if (chatRoomListResponseDto.getId() == roomId) {
                chatRoomListResponseDto.plusCount();
                chatRoomList.set(i, chatRoomListResponseDto);
                break;
            }
        }

        chatRedisRepository.saveChatRoomList(offlineUsers.getFirst().getId(), chatRoomList);
    }
}
