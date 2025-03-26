package com.ll.nbe344team7.global.security.dto


/**
 * CustomUserDetails 에서 user 데이터를 담을 dto
 *
 * @since 2025-03-26
 * @author 이광석
 */
data class CustomUserData(
    val memberId : Long,
    val username : String,
    val role : String,
    val password: String,
)
