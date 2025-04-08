package com.ll.nbe344team7.domain.alarm.entity

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO
import com.ll.nbe344team7.domain.member.entity.Member
import jakarta.annotation.Generated
import jakarta.persistence.*
import java.time.LocalDateTime


/**
 * 알람 엔티티
 *
 * @author 이광석
 * @since 2025-04-03
 */
@Entity
class Alarm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member : Member,

    val content : String,

    val type : Int,

    val checked : Boolean?= false,

    @Column(name = "created_at")
    val createdAt : LocalDateTime?=LocalDateTime.now(),
) {
    constructor(member: Member,content: String,type: Int) :this(
        id=null,
        member=member,
        content = content,
        type=type,
        checked=null,
        createdAt = null
    )
}