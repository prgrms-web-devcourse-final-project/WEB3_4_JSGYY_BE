package com.ll.nbe344team7.domain.member.dto


import com.ll.nbe344team7.domain.member.entity.Member
import io.swagger.v3.oas.annotations.media.Schema


/**
 * 멤버 DTO
 *
 * @author 이광석
 * @since 25.03.25
 */
@Schema(description = "회원가입 DTO")
data class MemberDTO(
    @field:Schema(description = "회원 ID", example = "1")
    val id : Long? = null,
    @field:Schema(description = "이름", example = "홍길동")
    val name: String="",
    @field:Schema(description = "사용자명", example = "gildong123")
    val username: String="",
    @field:Schema(description = "비밀번호", example = "1234")
    val password:String="",
    @field:Schema(description = "비밀번호 확인", example = "1234")
    val password2: String="",
    @field:Schema(description = "닉네임", example = "길동이")
    var nickname:String="",
    @field:Schema(description = "이메일", example = "test@example.com")
    val email:String="",
    @field:Schema(description = "전화번호", example = "010-1234-5678")
    var phoneNum: String="",
    @field:Schema(description = "권한", example = "ROLE_USER")
    val role:String="ROLE_ADMIN",
    @field:Schema(description = "주소", example = "서울시 강남구")
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