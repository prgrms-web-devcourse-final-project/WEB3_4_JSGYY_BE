package com.ll.nbe344team7.domain.post.dto

import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val authorId: Long,
    val title: String,
    val content: String,
    val category: String,
    val place: String,
    val price: Long,
    val saleStatus: Boolean,
    val auctionStatus: Boolean,
    val auctionStartedAt: LocalDateTime?,
    val auctionClosedAt: LocalDateTime?,
    val likes: Int,
    val reports: Int,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
    val isAuthor: Boolean,
    val images: List<PostImageDto> // 전체 이미지 리스트 포함
)