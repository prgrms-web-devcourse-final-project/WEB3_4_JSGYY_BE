package com.ll.nbe344team7.domain.chat.room.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author kjm72
 * @since 2025-03-26
 */
@Schema
data class ChatRoomListResponseDto(
    @field:Schema(description = "채팅방 ID", example = "1")
    val id : Long,
    @field:Schema(description = "채팅방 제목(상대 유저 닉네임)", example = "nickname2")
    val title : String,
    @field:Schema(description = "마지막 메시지를 보낸 유저 닉네임", example = "nickname1")
    val nickname : String,
    @field:Schema(description = "마지막 메시지", example = "ㅎㅇ")
    val lastMessage : String,
    @field:Schema(description = "읽지 않은 메시지의 개수", example = "3")
    var unReadCount: Long
){
    fun plusCount() {
        unReadCount++
    }

    constructor():this(0,"","","", 0)
}
