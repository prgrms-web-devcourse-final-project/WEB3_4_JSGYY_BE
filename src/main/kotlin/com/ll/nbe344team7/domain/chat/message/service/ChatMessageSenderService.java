package com.ll.nbe344team7.domain.chat.message.service;

import com.ll.nbe344team7.domain.alarm.service.AlarmService;
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
import com.ll.nbe344team7.global.config.redis.publisher.RedisPublisher;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    private final RedisPublisher chatRedisPublisher;
    private final ChatRedisRepository chatRedisRepository;
    private final DistributedLock lock;
    private final AlarmService alarmService;

    public ChatMessageSenderService(ChatParticipantService chatParticipantService, ChatMessageRepository chatMessageRepository, RedisPublisher chatRedisPublisher, ChatRedisRepository chatRedisRepository, DistributedLock lock, AlarmService alarmService) {
        this.chatParticipantService = chatParticipantService;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRedisPublisher = chatRedisPublisher;
        this.chatRedisRepository = chatRedisRepository;
        this.lock = lock;
        this.alarmService = alarmService;
    }

    public void sendMessage(MessageDTO dto, Member member, ChatRoom chatRoom) {
        Long roomId = chatRoom.getId();

        try {
            lock.executeWithLock(roomId, () -> {
                process(dto, member, chatRoom);
            });
        } catch (LockAcquisitionException e) {
            log.error("메세지 전송 실패 - 락 획득 실패: {}", roomId);
        }
    }

    @Transactional
    public void process(MessageDTO dto, Member member, ChatRoom chatRoom) {
        Long roomId = chatRoom.getId();

        ChatMessage chatMessage = new ChatMessage(member, dto.getContent(), chatRoom);
        chatMessageRepository.save(chatMessage);

        List<ChatParticipant> chatParticipants = chatParticipantService.getChatParticipants(roomId);
        Set<String> chatroomUsers = chatRedisRepository.getChatroomUsers("chatroom:" + roomId + ":users");

        if (chatroomUsers.size() < 2) {
            List<ChatParticipant> offlineUsers = chatParticipants.stream()
                    .filter(p -> !chatroomUsers.contains(String.valueOf(p.getMember().getId())))
                    .toList();

            // 메세지 안읽음
            chatMessage.setRead(false);
            redisUpdateReadCount(offlineUsers, roomId);

            // 알림 전송 로직
            for (ChatParticipant chatParticipant : offlineUsers) {
                String content = member.getNickname() + ": " + dto.getContent();
                alarmService.createAlarm(content, chatParticipant.getMember().getId(), 0);
            }
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            int maxRetries = 3;
                            for (int i = 0; i < maxRetries; i++) {
                                try {
                                    chatRedisPublisher.publishMessage(dto, chatMessage);
                                    chatRedisPublisher.publishRoomListUpdate(member.getId(), chatParticipants);
                                } catch (Exception e) {
                                    try {
                                        Thread.sleep(100 & (i + 1));
                                    } catch (InterruptedException ie) {
                                        ie.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.error("Redis 발행 실패: {}", e.getMessage());
                        }
                    }
                }
        );
    }

    /**
     * 안읽은 메세지 count 처리
     *
     * @param offlineUsers
     * @param roomId
     * @author jyson
     * @since 25. 4. 7.
     */
    private void redisUpdateReadCount(List<ChatParticipant> offlineUsers, Long roomId) {
        offlineUsers.forEach(user -> {
            List<ChatRoomListResponseDto> rooms = chatRedisRepository.getChatRoomList(user.getMember().getId());
            rooms.stream()
                    .filter(room -> room.getId() == roomId)
                    .findFirst()
                    .ifPresent(room -> {
                        room.plusCount();
                        chatRedisRepository.saveChatRoomList(user.getMember().getId(), rooms);
                    });
        });
    }
}
