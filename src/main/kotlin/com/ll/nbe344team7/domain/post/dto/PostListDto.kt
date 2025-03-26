package com.ll.nbe344team7.domain.post.dto

import com.ll.nbe344team7.domain.post.entity.Post
import java.time.LocalDateTime

data class PostListDto(
    val id: Long?,
    val title: String,
    val place: String,
    val price: Long,
    val auctionStatus: Boolean,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(post: Post): PostListDto {
            return PostListDto(
                id = post.id,
                title = post.title,
                place = post.place,
                price = post.price,
                auctionStatus = post.auctionStatus,
                createdAt = post.createdAt
            )
        }
    }
}