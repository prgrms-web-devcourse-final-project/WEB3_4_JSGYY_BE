package com.ll.nbe344team7.domain.alarm.exception

import org.springframework.http.HttpStatus

/**
 * 알람 예외 처리 코드
 *
 * @author 이광석
 * @since 2025-04-03
 */
enum class AlarmExceptionCode(val status: HttpStatus, val code: String, val message: String){
    NOT_FOUND_ALARM(HttpStatus.NOT_FOUND,"NOT_FOUND_ALARM","해당 알람을 찾을 수 없습니다.")
}