package com.ll.nbe344team7.domain.chat.message.dto

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
data class MessageDTO(var content: String, var roomId: Long?, val memberId: String, var chatRoomId: Long?){
    constructor():this("",null, "", null)
}