package com.ll.nbe344team7.domain.chat.message.service;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageDTO;
import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import com.ll.nbe344team7.domain.chat.message.repository.ChatMessageRepository;
import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import com.ll.nbe344team7.domain.chat.participant.service.ChatParticipantService;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom;
import com.ll.nbe344team7.domain.chat.room.repository.ChatRoomRedisRepository;
import com.ll.nbe344team7.domain.chat.room.service.ChatRoomRedisService;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.ll.nbe344team7.global.redis.RedisRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomService chatroomService;
    private final ChatParticipantService chatParticipantService;
    private final RedisRepository redisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;
    private final RedissonClient redissonClient;
    private final ChatRoomRedisService chatRoomRedisService;
    private final ChatRoomRedisRepository chatRoomRedisRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatroomService chatroomService, ChatParticipantService chatParticipantService, RedisRepository redisRepository, RedisTemplate<String, Object> redisTemplate, MemberRepository memberRepository, RedissonClient redissonClient, ChatRoomRedisService chatRoomRedisService, ChatRoomRedisRepository chatRoomRedisRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatroomService = chatroomService;
        this.chatParticipantService = chatParticipantService;
        this.redisRepository = redisRepository;
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
        this.redissonClient = redissonClient;
        this.chatRoomRedisService = chatRoomRedisService;
        this.chatRoomRedisRepository = chatRoomRedisRepository;
    }

    /**
     * 채팅 보내기
     *
     * 1. 채팅방 있는지 확인
     * 2. 채팅방에 자신이 포함되어 있는지 확인해야 될까?????
     *
     * @param dto
     * @param memberId

     *
     * @author jyson
     * @since 25. 3. 25.
     */
    @Transactional
    public void send(MessageDTO dto, Long memberId) {
        Long roomId = dto.getRoomId();
        String lockKey = "chat_lock:" + roomId; // 채팅방별 락 키 생성

        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 시도 (최대 10초 대기, 60초 후 자동 해제)
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);

            if (isLocked) {
                ChatRoom chatRoom = chatroomService.getChatRoom(roomId);
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

            ChatMessage chatMessage = new ChatMessage(member,dto.getContent(), chatRoom);

            chatMessageRepository.save(chatMessage);
            redisTemplate.convertAndSend("chatroom", new ChatMessageDTO(chatMessage));

            chatRoomRedisService.saveLastMessage(dto,chatMessage.getMember().getId());
            List<ChatParticipant> chatParticipants = chatParticipantService.getChatParticipants(dto.getRoomId());
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto();
            for (ChatParticipant chatParticipant : chatParticipants) {
                Long participantId = chatParticipant.getMember().getId();

                if (!participantId.equals(memberId)) {
                    List<ChatRoomListResponseDto> chatRoomList = chatRoomRedisService.getChatRooms(participantId);
                    chatRoomListDto = new ChatRoomListDto(participantId, chatRoomList);
                }
            }
            redisTemplate.convertAndSend("chatroomList", chatRoomListDto);
        }

        Set<String> chatroomUsers = redisRepository.getChatroomUsers("chatroom:" + roomId + ":users");

        if (chatroomUsers.size() < 2) {
            List<ChatParticipant> offlineUsers = chatParticipants.stream()
                    .filter(p -> !chatroomUsers.contains(String.valueOf(p.getMember().getId())))
                    .toList();

            chatMessage.setRead(false);

            List<ChatRoomListResponseDto> chatRoomList = chatRoomRedisRepository.getChatRoomList(offlineUsers.getFirst().getId());

            for (int i = 0; i < chatRoomList.size(); i++) {
                ChatRoomListResponseDto chatRoomListResponseDto = chatRoomList.get(i);
                if (chatRoomListResponseDto.getId() == roomId) {
                    chatRoomListResponseDto.plusCount();
                    chatRoomList.set(i, chatRoomListResponseDto);
                    break;
                }
            }

            chatRoomRedisRepository.saveChatRoomList(offlineUsers.getFirst().getId(), chatRoomList);
            // 알림 전송 로직
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 획득 중단", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock(); // 현재 스레드가 보유한 락 해제
            }
        }
    }

    /**
     * 채팅방 메세지 조회
     *
     * @param roomId
     * @param message
     * @param page
     * @param size
     * @return

     *
     * @author jyson
     * @since 25. 3. 25.
     * */
    public Page<ChatMessageDTO> getChatMessages(long roomId, String message, int page, int size) {
        chatroomService.getChatRoom(roomId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (!message.isEmpty()) return chatMessageRepository.findByChatRoomIdAndContentContaining(pageable, roomId, message).map(ChatMessageDTO::new);
        return chatMessageRepository.findByChatRoomId(pageable, roomId).map(ChatMessageDTO::new);
    }

}
