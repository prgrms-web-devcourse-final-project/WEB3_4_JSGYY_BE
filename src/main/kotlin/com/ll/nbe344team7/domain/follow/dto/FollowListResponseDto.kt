package com.ll.nbe344team7.domain.follow.dto

import com.ll.nbe344team7.domain.member.entity.Member
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author kjm72
 * @since 2025-03-24
 */
@Schema(description = "팔로잉 목록 응답 DTO")
data class FollowListResponseDto(
    @field:Schema(description = "팔로잉하고 있는 유저 ID", example = "2")
    val followingId : Long,
    @field:Schema(description = "팔로잉하고 있는 유저 닉네임", example = "nickname2")
    val nickname : String
){
    constructor() : this(0, "")

    constructor(member : Member) : this(
        member.id!!,
        member.nickname
    )
}
