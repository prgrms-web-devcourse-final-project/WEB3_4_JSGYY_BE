package com.ll.nbe344team7.domain.follow.dto

/**
 * Follow request dto
 *
 * @property userId
 * @property followingId
 * @constructor Create empty Follow request dto
 */
data class FollowRequestDto(val userId : Long, val followingId : Long)
