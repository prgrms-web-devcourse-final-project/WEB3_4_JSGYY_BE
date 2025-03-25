package com.ll.nbe344team7.domain.member.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name="member")
class MemberEntity (

    @Column(nullable = false,unique=true)
    val id: Long? = null,


    @Column(nullable = false,unique=true)
    val userName: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable=false)
    val nickName: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, name = "phone_num")
    val phoneNum: String,


    val blocked: Boolean,

    val role: Int
){
}
