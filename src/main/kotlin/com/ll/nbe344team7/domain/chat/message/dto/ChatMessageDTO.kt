package com.ll.nbe344team7.domain.chat.message.dto

import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
@Schema(description = "채팅 메시지 DTO")
data class ChatMessageDTO(
    @field:Schema(description = "채팅 메시지 ID", example = "1")
    val id: Long?,
    @field:Schema(description = "보내는 맴버 ID", example = "1")
    var memberId: Long?,
    @field:Schema(description = "채팅방 ID", example = "1")
    var chatroomId: Long?,
    @field:Schema(description = "메시지 내용", example = "ㅎㅇ")
    var content: String,
    @field:Schema(description = "생성 날짜", example = "2025-04-11T02:44:42.524Z")
    var createdAt: LocalDateTime?,
) {
    constructor() : this(null, null, null, "", null)

    constructor(chatMessage: ChatMessage?) : this(
        chatMessage?.id,
        chatMessage?.member?.id!!,
        chatMessage.chatRoom.id!!,
        chatMessage.content,
        chatMessage.createdAt
    )
}