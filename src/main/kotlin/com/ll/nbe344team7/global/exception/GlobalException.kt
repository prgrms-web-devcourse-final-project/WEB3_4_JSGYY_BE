package com.ll.nbe344team7.global.exception

import org.springframework.http.HttpStatus

/**
 *
 * 글로벌 에러 처리
 *
 * @author shjung
 * @since 25. 3. 25.
 */
class GlobalException(globalExceptionCode: GlobalExceptionCode): RuntimeException(globalExceptionCode.message) {
    val status: HttpStatus = globalExceptionCode.status
    val code: String = globalExceptionCode.code
}