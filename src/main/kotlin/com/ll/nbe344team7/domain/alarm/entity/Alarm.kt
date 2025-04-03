package com.ll.nbe344team7.domain.alarm.entity

import com.ll.nbe344team7.domain.alarm.dto.AlarmDTO
import com.ll.nbe344team7.domain.member.entity.Member
import jakarta.annotation.Generated
import jakarta.persistence.*
import java.time.LocalDateTime

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

    val isCheck : Boolean?= false,

    @Column(name = "created_at", nullable = false)
    val createdAt : LocalDateTime?=LocalDateTime.now(),
) {

}