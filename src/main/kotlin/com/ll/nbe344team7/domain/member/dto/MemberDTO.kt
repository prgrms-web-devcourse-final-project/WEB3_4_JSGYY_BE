package com.ll.nbe344team7.domain.member.dto


import com.ll.nbe344team7.domain.member.entity.Member


/**
 * 멤버 DTO
 *
 * @author 이광석
 * @since 25.03.25
 */
data class MemberDTO(
    val id : Long? = null,
    val name: String="",
    val username: String="",
    val password:String="",
    val password2: String="",
    var nickname:String="",
    val email:String="",
    var phoneNum: String="",
    val role:String="ROLE_ADMIN",
    var address: String = ""
){
    constructor(member: Member) :this(
        id=member.id,
        name = member.name,
        username = member.username,
        password = member.password,
        nickname = member.nickname,
        email = member.email,
        phoneNum = member.phoneNum,
        role = member.role,
        address = member.address
    )
    constructor() : this(null, "", "", "", "","","","")
}
