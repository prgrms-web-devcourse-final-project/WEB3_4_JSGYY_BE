package com.ll.nbe344team7.domain.chat.room.dto

/**
 *
 *
 * @author kjm72
 * @since 2025-03-26
 */
data class ChatRoomListResponseDto(
    val id : Long,
    val title : String,
    val nickname : String,
    val lastMessage : String
){
    constructor():this(0,"","","")
}
