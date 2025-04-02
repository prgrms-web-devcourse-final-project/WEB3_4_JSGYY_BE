package com.ll.nbe344team7.domain.post.dto.response

import com.ll.nbe344team7.domain.post.entity.Post
import java.time.LocalDateTime

data class PostListDto(
    val id: Long?,
    val title: String,
    val place: String,
    val price: Long,
    val saleStatus: Boolean,
    val auctionStatus: Boolean,
    val thumbnail: String?,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(post: Post): PostListDto {
            return PostListDto(
                id = post.id,
                title = post.title,
                place = post.place,
                price = post.price,
                saleStatus = post.saleStatus,
                auctionStatus = post.auctionStatus,
                thumbnail = post.images.firstOrNull()?.url,
                createdAt = post.createdAt
            )
        }
    }
}