package com.ll.nbe344team7.domain.member.exception

import org.springframework.http.HttpStatus

enum class MemberExceptionCode(val status:HttpStatus, val code: String,val message:String) {

}