package com.ll.nbe344team7.domain.chat.room.controller;


import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomListResponseDto;
import com.ll.nbe344team7.domain.chat.room.dto.ChatRoomRequestDto;
import com.ll.nbe344team7.domain.chat.room.dto.CreateResponseDto;
import com.ll.nbe344team7.domain.chat.room.service.ChatRoomRedisService;
import com.ll.nbe344team7.domain.chat.room.service.ChatroomService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 채팅방 컨트롤러
 *
 * @author kjm72
 * @since 2025-03-31
 */
@RestController
@RequestMapping("/api/chat/rooms")
@Tag(name = "채팅방 API")
public class ChatroomController {

    private final ChatroomService chatroomService;
    private final ChatRoomRedisService chatRoomRedisService;
    public ChatroomController(ChatroomService chatroomService, ChatRoomRedisService chatRoomRedisService) {
        this.chatroomService = chatroomService;
        this.chatRoomRedisService = chatRoomRedisService;
    }

    @Operation(
            summary = "채팅방 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "채팅방 생성 요청 Body",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "채팅방 생성 성공", content = @Content(mediaType = "application/json",schema = @Schema(implementation = CreateResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "해당 회원은 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<?> createChatroom(@RequestBody ChatRoomRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(chatroomService.createRoom(requestDto,userDetails.getMemberId()));
    }

    @Operation(
            summary = "채팅방 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChatRoomListResponseDto.class)))),
                    @ApiResponse(responseCode = "404", description = "채팅방 목록이 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/user")
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomListResponseDto> response = chatRoomRedisService.getChatRooms(userDetails.getMemberId());
        return ResponseEntity.ok(Map.of("rooms", response));
    }

    @Operation(
            summary = "채팅방 삭제",
            parameters = {
                    @Parameter(name = "id", description = "채팅방 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "채팅방 삭제 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "해당 채팅방이 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChatroom(@PathVariable("id") Long roomId) {
        chatroomService.deleteChatroom(roomId);
        return ResponseEntity.ok(Map.of("message","채팅방 삭제 성공"));
    }
}
