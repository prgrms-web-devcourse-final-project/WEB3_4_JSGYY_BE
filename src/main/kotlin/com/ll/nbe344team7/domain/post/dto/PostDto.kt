package com.ll.nbe344team7.domain.post.dto

import java.time.LocalDateTime

data class PostDto (
    val id: Long,
    val authorId: Long,
    val title: String,
    val content: String,
    val place: String,
    val price: Int,
    val status: Boolean,
    val auctionStatus: Boolean,
    val likes: Int,
    val reports: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)