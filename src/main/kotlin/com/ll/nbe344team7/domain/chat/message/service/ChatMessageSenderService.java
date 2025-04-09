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
import com.ll.nbe344team7.global.config.redis.publisher.RedisPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

//class Message{
//    DateTime created_at;
//}
//class User{}
//class Room{}
//class RoomAttendant{
//    Room room;
//    User user;
//    DateTime joined_at;
//}
//class MessageCheck{
//    Message message;
//    RoomAttendant attendant;
//}
//자바에서 동시처리능력은 == 스레드 코어의개수
//저런작업이 길어지면길어질수록 하나의리퀘스트가 오래동안잡혀있으니
// A 0.1초에메세지를보냄 A의메세지요청 0.4초에들어오고
// B 0.1초에메세지를보냄 B의메세지요청 0.3초에들어옴
// RaceCondition 이필요한작업들 아니면 비동기인데 순차적으로실행되어야하는작업들
// 티케팅, 게임서버같은경우에는 멀티플레이()
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
    private final AlarmService alarmService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomRedisService chatRoomRedisService;

    public ChatMessageSenderService(ChatParticipantService chatParticipantService, ChatMessageRepository chatMessageRepository, RedisPublisher chatRedisPublisher, ChatRedisRepository chatRedisRepository, AlarmService alarmService, RedisTemplate<String, Object> redisTemplate, ChatRoomRedisService chatRoomRedisService) {
        this.chatParticipantService = chatParticipantService;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRedisPublisher = chatRedisPublisher;
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
        //fallback처리?
        //1. 레디스로부터 참가자정보를읽어옴
        //2. 전파된메세지저장 ->  참가자들에게게메세지전파

        //3. * 알람저장 -> 알람전파 < 비동기처리
        //4.
        Long roomId = chatRoom.getId();
        // save message to db
        chatMessageRepository.save(chatMessage);
        chatRoomRedisService.saveLastMessage(dto,chatMessage.getMember().getId());
        //
        redisTemplate.convertAndSend("chatroom", new ChatMessageDTO(chatMessage));

        handleAlarm(dto, roomId, member, chatMessage, chatParticipants);
    }

    @Async
    public void handleAlarm(MessageDTO dto, Long roomId, Member member, ChatMessage chatMessage,List<ChatParticipant> chatParticipants){
        Set<String> chatroomUsers = chatRedisRepository.getChatroomUsers("chatroom:" + roomId + ":users");

        // 채팅방에 없을 때 안읽음, 알람 처리
        if (chatroomUsers.size() < 2) {

            List<ChatParticipant> offlineUsers = chatParticipants.stream()
                    .filter(p -> !chatroomUsers.contains(String.valueOf(p.getMember().getId())))
                    .toList();

            for (ChatParticipant chatParticipant : offlineUsers) {
                Long participantId = chatParticipant.getMember().getId();
                List<ChatRoomListResponseDto> chatRoomList = chatRoomRedisService.getChatRooms(participantId);
                redisTemplate.convertAndSend("chatroomList", new ChatRoomListDto(participantId, chatRoomList));
            }

            chatMessage.setRead(false);

            List<ChatRoomListResponseDto> chatRoomList = chatRedisRepository.getChatRoomList(offlineUsers.getFirst().getId());

            for (int i = 0; i < chatRoomList.size(); i++) {
                ChatRoomListResponseDto chatRoomListResponseDto = chatRoomList.get(i);
                if (chatRoomListResponseDto.getId() == roomId) {
                    chatRoomListResponseDto.plusCount();
                    chatRoomList.set(i, chatRoomListResponseDto);
                    break;
                }
            }

            // 알림 전송 로직
            for (ChatParticipant chatParticipant : offlineUsers) {
                String content = member.getNickname() + ": " + dto.getContent();
                alarmService.createAlarm(content, chatParticipant.getMember().getId(), 2);
            }
    }
}


//    private void redisPublish(MessageDTO dto, Member member, ChatMessage chatMessage, List<ChatParticipant> chatParticipants) {
//        TransactionSynchronizationManager.registerSynchronization(
//                new TransactionSynchronization() {
//                    @Override
//                    public void afterCommit() {
//                        try {
//                            int maxRetries = 3;
//                            for (int i = 0; i < maxRetries; i++) {
//                                try {
//                                    chatRedisPublisher.publishMessage(dto, chatMessage);
//                                    chatRedisPublisher.publishRoomListUpdate(member.getId(), chatParticipants);
//                                    return;
//                                } catch (Exception e) {
//                                    log.warn("재시도 {}회 실패: {}", i+1, e.getMessage());
//                                    backoff(i);
//                                }
//                            }
//                        } catch (Exception e) {
//                            log.error("Redis 발행 실패: {}", e.getMessage());
//                        }
//                    }
//                }
//        );
//    }
//
//    private void backoff(int i) {
//        try {
//            Thread.sleep(100L * (int) Math.pow(2, i));
//        } catch (InterruptedException ie) {
//            ie.printStackTrace();
//        }
//    }
}
