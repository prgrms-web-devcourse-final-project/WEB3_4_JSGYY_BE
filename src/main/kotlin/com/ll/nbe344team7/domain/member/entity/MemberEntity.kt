package com.ll.nbe344team7.domain.member.entity

import com.ll.nbe344team7.domain.member.dto.MemberDTO
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name="member")
class MemberEntity(

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
    val nickName: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, name = "phone_num")
    val phoneNum: String,

    @Column(nullable = false)
    val blocked: Boolean =false,

    @Column(nullable = false)
    val role: Int = 2
):BaseEntity(){
   constructor(dto: MemberDTO):this(
       id=null,
       name= dto.name,
       userName=dto.username,
       password=dto.password,
       nickName=dto.nickName,
       email=dto.email,
       phoneNum=dto.phone_num,
       blocked=false,
       role=2
   )
}
