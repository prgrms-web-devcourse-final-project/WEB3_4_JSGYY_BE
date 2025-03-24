package com.ll.nbe344team7.domain.member.dto

data class MemberDTO(
    val name: String,
    val username: String,
    val password: String,
    val password2: String,
    val nickName: String,
    val email: String,
    val phone_num: String
)
