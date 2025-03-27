package com.ll.nbe344team7.domain.member.entity

import com.ll.nbe344team7.domain.member.dto.MemberDTO
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*

/**
 * 멤버 entity
 *
 * @author 이광석
 * @since 25.03.25
 */
@Entity
@Table(name="member")
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,


    @Column(nullable = false,unique=true)
    val userName: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable=false, unique = true)
    val nickname: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, name = "phone_num")
    val phoneNum: String,

    @Column(nullable = false)
    val blocked: Boolean =false,

    @Column(nullable = false)
    val role: String = "ROLE_ADMIN"
):BaseEntity(){
   constructor(dto: MemberDTO):this(
       id=null,
       name= dto.name,
       userName=dto.username,
       password=dto.password,
       nickname=dto.nickName,
       email=dto.email,
       phoneNum=dto.phone_num,
       blocked=false,
       role=dto.role
   )

    constructor() : this(
        id = null,
        userName = "",
        name = "",
        password = "",
        nickname = "",
        email = "",
        phoneNum = "",
        blocked = false,
        role = "ROLE_ADMIN"
    )
}
