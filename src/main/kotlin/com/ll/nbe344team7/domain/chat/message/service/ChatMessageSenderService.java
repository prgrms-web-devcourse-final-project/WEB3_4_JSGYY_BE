package com.ll.nbe344team7.domain.chat.message.service;

import com.ll.nbe344team7.domain.alarm.service.AlarmService;
import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.message.repository.ChatMessageRepository;
import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.service.ChatParticipantService;
import com.ll.nbe344team7.domain.chat.redis.repository.ChatRedisRepository;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.service.ChatRoomRedisService;
import com.ll.nbe344team7.domain.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
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
    private final ChatRedisRepository chatRedisRepository;
    private final AlarmService alarmService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRedisService chatRoomRedisService;

    public ChatMessageSenderService(ChatParticipantService chatParticipantService, ChatMessageRepository chatMessageRepository, ChatRedisRepository chatRedisRepository, AlarmService alarmService, RedisTemplate<String, Object> redisTemplate, ChatRoomRedisService chatRoomRedisService) {
        this.chatParticipantService = chatParticipantService;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRedisRepository = chatRedisRepository;
        this.alarmService = alarmService;
        this.redisTemplate = redisTemplate;
        this.chatRoomRedisService = chatRoomRedisService;
    }

    public void sendMessage(MessageDTO dto, Member member, ChatRoom chatRoom) {
        Long roomId = chatRoom.getId();
        ChatMessage chatMessage = new ChatMessage(member, dto.getContent(), chatRoom);
        List<ChatParticipant> chatParticipants = chatParticipantService.getChatParticipants(roomId);

        process(dto, member, chatRoom, chatMessage, chatParticipants);
    }

    @Transactional
    public void process(MessageDTO dto, Member member, ChatRoom chatRoom, ChatMessage chatMessage, List<ChatParticipant> chatParticipants) {

        //1. 레디스로부터 참가자정보를읽어옴
        //2. 전파된메세지저장 ->  참가자들에게게메세지전파
        //3. 메세지 읽지 않음 저장 -> 채팅목록 리스트 전파 < 비동기처리
        //4. 알람저장 -> 알람전파 < 비동기처리

        long start = System.currentTimeMillis();
        Long roomId = chatRoom.getId();
        // 메세지 보내기
        saveMessage(dto, chatMessage);
        long end = System.currentTimeMillis();
        log.info("process time: {}", end - start);

        // 비동기 알람 처리
        handleAlarm(dto, roomId, member, chatMessage, chatParticipants);
    }

    /**
     * 메세지 발행 처리
     * @param dto
     * @param chatMessage

     *
     * @author jyson
     * @since 25. 4. 14.     */
    private void saveMessage(MessageDTO dto, ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        chatRoomRedisService.saveLastMessage(dto, chatMessage.getMember().getId());

        redisPublish("chatroom", new ChatMessageDTO(chatMessage));
    }

    /**
     * 채팅방 리스트 목록, 알람 발행
     * @param dto
     * @param roomId
     * @param member
     * @param chatMessage
     * @param chatParticipants

     *
     * @author jyson
     * @since 25. 4. 14.     */
    @Async
    public void handleAlarm(MessageDTO dto, Long roomId, Member member, ChatMessage chatMessage, List<ChatParticipant> chatParticipants) {
        long start = System.currentTimeMillis();
        Set<String> chatroomUsers = chatRedisRepository.getChatroomUsers("chatroom:" + roomId + ":users");

        List<Long> offlineUsers = chatParticipants.stream()
                .filter(p -> !chatroomUsers.contains(p.getMember().getId().toString()))
                .map(p -> p.getMember().getId())
                .toList();

        if (offlineUsers.isEmpty()) return;

        // 채팅방에 없을 때 안읽음, 알람 처리
        chatMessage.setRead(false);
        chatMessageRepository.save(chatMessage);

        for (Long participantId : offlineUsers) {
            // 안읽음 메세지 카운터 처리, 채팅방 목록 보내기
            redisUpdateReadCount(roomId, participantId);

            String content = member.getNickname() + ": " + dto.getContent();
            alarmService.createAlarm(content, participantId, 2, roomId);
        }

        long end = System.currentTimeMillis();
        log.info("handleAlarm time: {}", end - start);
    }

    /**
     * 안읽음 메세지 카운터 처리, 채팅방 목록 보내기
     * @param roomId
     * @param participantId

     *
     * @author jyson
     * @since 25. 4. 14.     */
    private void redisUpdateReadCount(Long roomId, Long participantId) {
        List<ChatRoomListResponseDto> chatRoomList = chatRoomRedisService.getChatRooms(participantId);

        // 안읽음 메세지 카운트
        for (int i = 0; i < chatRoomList.size(); i++) {
            ChatRoomListResponseDto chatRoomListResponseDto = chatRoomList.get(i);
            if (chatRoomListResponseDto.getId() == roomId) {
                chatRoomListResponseDto.plusCount();
                chatRoomList.set(i, chatRoomListResponseDto);
                break;
            }
        }

        chatRedisRepository.saveChatRoomList(participantId, chatRoomList);
        redisPublish("chatroomList", new ChatRoomListDto(participantId, chatRoomList));
    }

    /**
     * 일시적 장애 시 redis 발행 재시도
     *
     * @param channel
     * @param object

     *
     * @author jyson
     * @since 25. 4. 10. */
    private void redisPublish(String channel, Object object) {

        try {
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    redisTemplate.convertAndSend(channel, object);
                    return;
                } catch (Exception e) {
                    log.warn("재시도 {}회 실패: {}", i + 1, e.getMessage());
                    backoff(i);
                }
            }
        } catch (Exception e) {
            log.error("Redis 발행 실패: {}", e.getMessage());
        }
    }

    /**
     * 재시도 간격 100ms, 200ms, 400ms
     * @param i

     *
     * @author jyson
     * @since 25. 4. 10.     */
    private void backoff(int i) {
        try {
            Thread.sleep(100L * (int) Math.pow(2, i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
