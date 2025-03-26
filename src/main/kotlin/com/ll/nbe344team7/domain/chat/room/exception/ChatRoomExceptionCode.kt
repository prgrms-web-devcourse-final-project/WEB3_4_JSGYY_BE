package com.ll.nbe344team7.global.exception

import org.springframework.http.HttpStatus

/**
 *
 * 채팅룸 에러 코드
 *
 * @author shjung
 * @since 25. 3. 25.
 */
enum class ChatRoomExceptionCode(val status: HttpStatus, val code: String, val message: String) {
    NOT_FOUND_LIST(HttpStatus.NOT_FOUND, "NOT_FOUND_LIST", "채팅 목록이 존재하지 않습니다."),
    NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, "NOT_FOUND_ROOM", "해당 채팅방이 존재하지 않습니다.")
}