package com.ll.nbe344team7.domain.chat.message.dto

import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
data class ChatMessageDTO(
    val id: Long?,
    var memberId: Long?,
    var chatroomId: Long?,
    var content: String,
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