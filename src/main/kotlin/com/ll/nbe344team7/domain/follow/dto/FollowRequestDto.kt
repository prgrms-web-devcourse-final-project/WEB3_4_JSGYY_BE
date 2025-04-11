package com.ll.nbe344team7.domain.follow.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Follow request dto
 *
 * @property userId
 * @property followingId
 * @constructor Create empty Follow request dto
 */
@Schema(description = "팔로우/언팔로우 요청 API")
data class FollowRequestDto(
    @field: Schema(description = "팔로우/언팔로우할 유저 ID", example = "2")
    val followingId : Long
){
    constructor():this(0)
}
