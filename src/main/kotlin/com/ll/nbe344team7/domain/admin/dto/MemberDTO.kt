package com.ll.nbe344team7.domain.admin.dto

/**
 *
 * createdAt은 나중에 바꿔야함
 *
 * @author shjung
 * @since 25. 3. 24.
 */
data class MemberDTO (val name: String, val nickname: String, val email: String, val blocked: Boolean, val createdAt: String, val address: String, val phoneNumber: String)
/**
 * {
 * "name": "홍길동",
 * "nickname": "길동이",
 * "email": "hong@example.com",
 * "blocked": false,
 * "created_at": "2024-03-21T12:00:00",
 * "address": "서울특별시 강남구",
 * ”phone_num
 * },
 */