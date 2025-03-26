package com.ll.nbe344team7.global.exception

import org.springframework.http.HttpStatus

/**
 *
 * 채팅룸 에러 처리
 *
 * @author shjung
 * @since 25. 3. 25.
 */
class ChatRoomException(chatRoomExceptionCode: ChatRoomExceptionCode): RuntimeException(chatRoomExceptionCode.message) {
    val status: HttpStatus = chatRoomExceptionCode.status
    val code: String = chatRoomExceptionCode.code
}