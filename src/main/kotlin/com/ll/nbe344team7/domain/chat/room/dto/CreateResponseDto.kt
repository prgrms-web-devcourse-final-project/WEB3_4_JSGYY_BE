package com.ll.nbe344team7.domain.chat.room.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author kjm72
 * @since 2025-03-25
 */
@Schema(description = "채팅방 생성 응답 DTO")
data class CreateResponseDto(
    @field:Schema(description = "메세지")
    var message: String
)