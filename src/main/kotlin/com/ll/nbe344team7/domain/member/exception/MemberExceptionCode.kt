package com.ll.nbe344team7.domain.member.exception

import org.springframework.http.HttpStatus

enum class MemberExceptionCode(val status:HttpStatus, val code: String,val message:String) {
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "NOT_AUTHORIZED", "내 게시글만 조회할 수 있습니다."),

}