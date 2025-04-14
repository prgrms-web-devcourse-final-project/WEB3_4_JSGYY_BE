package com.ll.nbe344team7.domain.member.entity

import com.ll.nbe344team7.domain.alarm.entity.Alarm
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
    val username: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable=false, unique = true)
    var nickname: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, name = "phone_num")
    var phoneNum: String,

    @Column(nullable = false)
    val blocked: Boolean =false,

    @Column(nullable = false)
    val role: String = "ROLE_ADMIN",

    @Column(nullable = false)
    var address: String ="",

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], orphanRemoval = true)
val alarms: MutableList<Alarm> = mutableListOf()
):BaseEntity(){
   constructor(dto: MemberDTO):this(
       id=null,
       name= dto.name,
       username=dto.username,
       password=dto.password,
       nickname=dto.nickname,
       email=dto.email,
       phoneNum=dto.phoneNum,
       blocked=false,
       role=dto.role,
       address = dto.address
   )

    constructor() : this(
        id = null,
        username = "",
        name = "",
        password = "",
        nickname = "",
        email = "",
        phoneNum = "",
        blocked = false,
        role = "ROLE_ADMIN",
        address= ""
    )

    constructor(username: String,nickname: String,password: String):this(
        id=null,
        username = username,
        name="tmp",
        password = password,
        nickname=nickname,
        email="email",
        phoneNum="phoneNum",
        blocked=false,
        role="ROLE_ADMIN"
    )

    constructor(memberId: Long, nickname: String) : this(
        id=memberId,
        username = "",
        name="",
        password = "",
        nickname=nickname,
        email="",
        phoneNum="",
        blocked=false,
        role="ROLE_ADMIN"
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Member) return false
        return id == other.id // id가 같으면 동일 객체로 간주
    }

    override fun hashCode(): Int {
        return id.hashCode() // id의 해시코드를 반환
    }
}
