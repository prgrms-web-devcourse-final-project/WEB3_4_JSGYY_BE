package com.ll.nbe344team7.global.exception

import org.springframework.http.HttpStatus

/**
 *
 * 팔로우 에러 코드
 *
 * @author shjung
 * @since 25. 3. 25.
 */
enum class FollowExceptionCode(val status: HttpStatus, val code: String, val message: String) {
    ALREADY_FOLLOW(HttpStatus.NOT_FOUND, "ALREADY_FOLLOW", "이미 팔로우한 유저입니다."),
    NOT_EXIST_FOLLOW(HttpStatus.NOT_FOUND, "NOT_EXIST_FOLLOW", "팔로우하지 않은 유저입니다.")

}