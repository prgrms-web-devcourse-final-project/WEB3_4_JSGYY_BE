package com.ll.nbe344team7.domain.chat.room.dto

data class ChatRoomCreateResponseDto(
    val message: String
){
    companion object{
        fun success(): ChatRoomCreateResponseDto {
            return ChatRoomCreateResponseDto("채팅방 생성 성공")
        }
    }
}
