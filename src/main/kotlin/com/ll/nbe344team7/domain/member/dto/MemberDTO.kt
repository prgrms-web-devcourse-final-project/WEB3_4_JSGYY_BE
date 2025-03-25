package com.ll.nbe344team7.domain.member.dto

import com.ll.nbe344team7.domain.member.entity.MemberEntity

data class MemberDTO(
    val name: String="",
    val username: String="",
    val password:String="",
    val password2: String="",
    val nickName:String="",
    val email:String="",
    val phone_num: String=""
)
fun MemberDTO.toEntity(): MemberEntity {
    return MemberEntity(
        userName = this.username,
        name = this.name,
        password = this.password,
        nickName = this.nickName,
        email = this.email,
        phoneNum = this.phone_num,
        blocked = false,
        role = 2
    )
}