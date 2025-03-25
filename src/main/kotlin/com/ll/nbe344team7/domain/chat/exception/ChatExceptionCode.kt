package com.ll.nbe344team7.domain.chat.exception

import org.springframework.http.HttpStatus

/**
 *
 * 글로벌 에러 코드
 *
 * @author shjung
 * @since 25. 3. 25.
 */
enum class ChatExceptionCode(val status: HttpStatus, val code: String, val message: String) {
    NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, "NOT_FOUND_ROOM", "채팅방이 조회되지 않습니다.")
}