package com.ll.nbe344team7.domain.follow.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 팔로우, 언팔로우 Res DTO
 *
 * @author kjm72
 * @since 2025-03-24
 */
@Schema(description = "팔로우/언팔로우 응답 DTO")
data class FollowResponseDto(
    @field:Schema(description = "응답 메시지")
    val message : String
){
    constructor():this("")
}
