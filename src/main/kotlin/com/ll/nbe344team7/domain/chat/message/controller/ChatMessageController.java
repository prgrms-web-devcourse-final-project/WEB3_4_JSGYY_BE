package com.ll.nbe344team7.domain.chat.message.controller;

import com.ll.nbe344team7.domain.chat.message.dto.ChatMessageDTO;
import com.ll.nbe344team7.domain.chat.message.dto.MessageSearchDTO;
import com.ll.nbe344team7.domain.chat.message.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author jyson
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/chat/rooms/{roomId}")
@Tag(name = "채팅 메시지 API")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    /**
     * 메세지 보내기 컨트롤러
     *
     * @param messageDTO
     * @param roomId
     * @return
     * @author jyson
     * @since 25. 3. 25.
     */
    @Operation(
            summary = "메시지 전송",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "채팅 메시지 Body",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatMessageDTO.class)
                    )
            ),
            parameters = {
                    @Parameter(name = "roomId", description = "채팅방 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "메시지 전송 성공", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "맴버가 조회되지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageDTO messageDTO,
            @PathVariable long roomId
    ) {
        chatMessageService.send(messageDTO, roomId);

        return ResponseEntity.ok(Map.of(
                "message", "메세지 전송완료"
        ));
    }

    /**
     * 채팅방 입장 시 채팅 조회
     *
     * @param roomId
     * @param messageSearchDTO
     * @return
     * @author jyson
     * @since 25. 3. 25.
     */
    @Operation(
            summary = "채팅방 입장 시 채팅 조회",
            parameters = {
                    @Parameter(name = "roomId", description = "채팅방 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "채팅 메시지 조회 성공",content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "성공 응답 예시",
                                    value = """
                                    {
                                      "content": [
                                        {
                                          "id": 1,
                                          "memberId": 1,
                                          "chatroomId": 2,
                                          "content": "메세지",
                                          "createdAt": "2025-04-11T13:26:20.705828"
                                        }
                                      ],
                                      "pageable": {
                                        "pageNumber": 0,
                                        "pageSize": 10,
                                        "sort": {
                                          "empty": false,
                                          "sorted": true,
                                          "unsorted": false
                                        },
                                        "offset": 0,
                                        "paged": true,
                                        "unpaged": false
                                      },
                                      "last": true,
                                      "totalElements": 1,
                                      "totalPages": 1,
                                      "size": 10,
                                      "number": 0,
                                      "sort": {
                                        "empty": false,
                                        "sorted": true,
                                        "unsorted": false
                                      },
                                      "first": true,
                                      "numberOfElements": 1,
                                      "empty": false
                                    }
                                    """)
                    )),
                    @ApiResponse(responseCode = "404", description = "해당 채팅방이 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public ResponseEntity<?> enterRoom(
            @PathVariable long roomId,
            @ModelAttribute MessageSearchDTO messageSearchDTO
            ) {
        Page<ChatMessageDTO> chats = chatMessageService.getChatMessages(roomId, messageSearchDTO);

        return ResponseEntity.ok(chats);
    }
}
