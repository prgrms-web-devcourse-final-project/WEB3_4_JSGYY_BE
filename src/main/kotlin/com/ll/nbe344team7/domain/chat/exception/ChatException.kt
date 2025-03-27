package com.ll.nbe344team7.domain.chat.exception

import org.springframework.http.HttpStatus

/**
 *
 * 글로벌 에러 처리
 *
 * @author shjung
 * @since 25. 3. 25.
 */
class ChatException(chatExceptionCode: ChatExceptionCode): RuntimeException(chatExceptionCode.message) {
    val status: HttpStatus = chatExceptionCode.status
    val code: String = chatExceptionCode.code
}