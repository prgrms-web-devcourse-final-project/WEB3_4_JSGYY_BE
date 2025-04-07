package com.ll.nbe344team7.global.exception

import org.springframework.http.HttpStatus

/**
 *
 * 채팅룸 에러 처리
 *
 * @author shjung
 * @since 25. 3. 25.
 */
class FollowException(followExceptionCode: FollowExceptionCode): RuntimeException(followExceptionCode.message) {
    val status: HttpStatus = followExceptionCode.status
    val code: String = followExceptionCode.code
}