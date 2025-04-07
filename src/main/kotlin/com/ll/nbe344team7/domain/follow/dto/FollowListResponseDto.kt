package com.ll.nbe344team7.domain.follow.dto

import com.ll.nbe344team7.domain.member.entity.Member

data class FollowListResponseDto(
    val followingId : Long,
    val nickname : String
){
    constructor() : this(0, "")

    constructor(member : Member) : this(
        member.id!!,
        member.nickname
    )
}
