package com.ll.nbe344team7.global.security.exception

import org.springframework.http.HttpStatus

class SecurityException(exceptionCode: SecurityExceptionCode):RuntimeException(exceptionCode.message) {
    val status: HttpStatus = exceptionCode.status
    val code : String =exceptionCode.code
}