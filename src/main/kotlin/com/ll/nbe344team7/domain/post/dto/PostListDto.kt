package com.ll.nbe344team7.domain.post.dto

import java.time.LocalDateTime

data class PostListDto(
    val id: Long,
    val title: String,
    val price: Long,
    val saleStatus: Boolean,
    val thumbnail: String?, // 대표 이미지 URL (썸네일)
    val createdAt: LocalDateTime // 생성 날짜
)
