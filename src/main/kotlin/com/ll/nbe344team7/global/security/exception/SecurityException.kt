package com.ll.nbe344team7.global.security.exception

import org.springframework.http.HttpStatus

/**
 * 시큐리지 전용 예외처리
 *
 * @author 이광석
 * @since 2025-03-28
 */
class SecurityException(exceptionCode: SecurityExceptionCode):RuntimeException(exceptionCode.message) {
    val status: HttpStatus = exceptionCode.status
    val code : String =exceptionCode.code
}