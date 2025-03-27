package com.ll.nbe344team7.global.exception

import org.springframework.http.HttpStatus

/**
 *
 * 글로벌 에러 코드
 *
 * @author shjung
 * @since 25. 3. 25.
 */
enum class GlobalExceptionCode(val status: HttpStatus, val code: String, val message: String) {
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NOT_FOUND_MEMBER", "멤버가 조회되지 않습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "NOT_FOUND_POST", "게시글이 조회되지 않습니다."),
    ACCESSTOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCESSTOKEN_IS_EXPIRED","ACCESSTOKEN이 만료되었습니다"),
    NOT_ACCESSTOKEN(HttpStatus.UNAUTHORIZED,"NOT_ACCESSTOKEN","해당 토큰은 ACCESSTOKEN이 아닙니다")
}