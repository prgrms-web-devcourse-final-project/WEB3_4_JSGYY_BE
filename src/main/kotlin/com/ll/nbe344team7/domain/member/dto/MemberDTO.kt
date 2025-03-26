package com.ll.nbe344team7.domain.member.dto

import com.ll.nbe344team7.domain.member.entity.MemberEntity

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
    val nickName:String="",
    val email:String="",
    val phone_num: String="",
    val role:String="ROLE_ADMIN"
)
