package com.ll.nbe344team7.domain.chat.room.dto

/**
 * ChatRoomListResponseDto와 memberId를 받기 위한 DTO
 *
 * @author kjm72
 * @since 2025-04-03
 */
data class ChatRoomListDto(
    val memberId : Long,
    val chatRooms : List<ChatRoomListResponseDto>
){
    constructor():this(0,listOf())
}
