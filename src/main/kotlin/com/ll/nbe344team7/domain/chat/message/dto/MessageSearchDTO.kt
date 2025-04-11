package com.ll.nbe344team7.domain.chat.message.dto

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
data class MessageSearchDTO(var message: String, var page: Int, var size: Int){
    constructor():this("",0,10)
}