package com.ll.nbe344team7.domain.member.dto

/**
 * 멤버 DTO
 *
 * @author 이광석
 * @since 25.03.25
 */
data class MemberDTO(
    val id : Long? ,
    val name: String="",
    val username: String="",
    val password:String="",
    val password2: String="",
    val nickname:String="",
    val email:String="",
    val phoneNum: String="",
    val role:String="ROLE_ADMIN"
)
