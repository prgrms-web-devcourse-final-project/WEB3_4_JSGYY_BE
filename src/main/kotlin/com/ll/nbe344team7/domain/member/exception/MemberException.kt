package com.ll.nbe344team7.domain.member.exception

import org.springframework.http.HttpStatus

class MemberException(exceptionCode: MemberExceptionCode):RuntimeException(exceptionCode.message) {
    val status : HttpStatus = exceptionCode.status
    val code : String = exceptionCode.code
}