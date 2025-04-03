package com.ll.nbe344team7.domain.alarm.exception

import org.springframework.http.HttpStatus

class AlarmException(exceptionCode: AlarmExceptionCode):RuntimeException(exceptionCode.message)  {
    val status : HttpStatus  = exceptionCode.status
    val cod : String =exceptionCode.code
}