package com.ll.nbe344team7.domain.chat.dto

import java.time.LocalDateTime

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
data class ChatMessageDTO(val id: Long, var memberId: Long, var chatroomId: Long, var content: String, var createdAt: LocalDateTime)