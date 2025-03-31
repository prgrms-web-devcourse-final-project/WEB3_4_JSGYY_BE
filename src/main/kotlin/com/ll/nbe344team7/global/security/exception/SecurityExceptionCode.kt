package com.ll.nbe344team7.global.security.exception

import org.springframework.http.HttpStatus

/**
 * 시큐리티 전용 예외 코드
 * @author 이광석
 * @since 2025-03-28
 */
enum class SecurityExceptionCode (
    val status:HttpStatus,
    val code:  String,
    val message: String
){
    //access 토큰 관련
    ACCESSTOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED, "ACCESSTOKEN_IS_EXPIRED","ACCESSTOKEN이 만료되었습니다"),
    NOT_ACCESSTOKEN(HttpStatus.UNAUTHORIZED,"NOT_ACCESSTOKEN","해당 토큰은 ACCESSTOKEN이 아닙니다"),

    //refresh 토큰 관련
    NOT_FOUND_REFRESHTOKEN(HttpStatus.NOT_FOUND,"NOT_FOUND_REFRESHTOKEN", "REFRESHTOKEN을 찾을 수 없습니다"),
    REFRESHTOKEN_IS_EXPIRED(HttpStatus.UNAUTHORIZED,"REFRESHTOKEN_IS_EXPIRED","REFRESHTOKEN이 만료되었습니다."),
    IS_NOT_REFRESHTOKEN(HttpStatus.UNAUTHORIZED, " IS_NOT_REFRESHTOKEN","해당 토큰은 REFRESHTOKEN이 아닙니다"),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "TOKEN_MISMATCH","DB의 토큰과 ACCESS 토큰이 일치하지 않습니다.");

}