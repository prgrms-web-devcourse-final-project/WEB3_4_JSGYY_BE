package com.ll.nbe344team7.domain.chat.room.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author kjm72
 * @since 2025-03-26
 */
@Schema(description = "채팅방 생성 DTD")
data class ChatRoomRequestDto(
    @field:Schema(description = "상대 유저 ID", example = "2")
    val userId : Long
){
    constructor(): this(0)
}