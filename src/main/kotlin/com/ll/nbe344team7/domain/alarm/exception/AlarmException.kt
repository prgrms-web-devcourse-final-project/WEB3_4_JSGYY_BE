package com.ll.nbe344team7.domain.alarm.exception

import org.springframework.http.HttpStatus


/**
 * 알람 전용 예외처리
 *
 * @author 이광석
 * @since 2025-04-03
 */
class AlarmException(exceptionCode: AlarmExceptionCode):RuntimeException(exceptionCode.message)  {
    val status : HttpStatus  = exceptionCode.status
    val code : String =exceptionCode.code
}