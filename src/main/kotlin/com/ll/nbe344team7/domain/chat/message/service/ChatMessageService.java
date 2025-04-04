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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
    private final ChatRoomRedisService chatRoomRedisService;
    private final ChatRoomRedisRepository chatRoomRedisRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatroomService chatroomService, ChatParticipantService chatParticipantService, RedisRepository redisRepository, RedisTemplate<String, Object> redisTemplate, MemberRepository memberRepository, ChatRoomRedisService chatRoomRedisService, ChatRoomRedisRepository chatRoomRedisRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatroomService = chatroomService;
        this.chatParticipantService = chatParticipantService;
        this.redisRepository = redisRepository;
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
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
    public void send(MessageDTO dto, Long memberId) {
        ChatRoom chatRoom = chatroomService.getChatRoom(dto.getRoomId());
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        ChatMessage chatMessage = new ChatMessage(member,dto.getContent(), chatRoom);
        System.out.println(chatMessage.getChatRoom()+chatMessage.getContent()+chatMessage.getMember());


        chatMessageRepository.save(chatMessage);
        redisTemplate.convertAndSend("chatroom", new ChatMessageDTO(chatMessage));
        chatRoomRedisService.saveLastMessage(dto,chatMessage.getMember().getId());
        List<ChatParticipant> chatParticipants = chatParticipantService.getChatParticipants(dto.getRoomId());
        for (ChatParticipant chatParticipant : chatParticipants) {
            Long participantId = chatParticipant.getMember().getId();
            chatRoomRedisRepository.deleteChatRoomList(participantId);
            List<ChatRoomListResponseDto> chatRoomList = chatRoomRedisService.getChatRooms(participantId);
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(memberId, chatRoomList);
            redisTemplate.convertAndSend("chatroomList", chatRoomListDto);
        }

        Long roomId = chatRoom.getId();
        Set<String> chatroomUsers = redisRepository.getChatroomUsers("chatroom:" + roomId + ":users");

        if (chatroomUsers.size() < 2) {
            List<ChatParticipant> offlineUsers = chatParticipants.stream()
                    .filter(p -> !chatroomUsers.contains(String.valueOf(p.getMember().getId())))
                    .toList();

            // offlineUsers 에 담긴 user 에게 알람 전송 코드 넣으면 됨
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
