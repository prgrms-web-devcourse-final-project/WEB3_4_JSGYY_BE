package com.ll.nbe344team7.domain.post.dto

import com.ll.nbe344team7.domain.post.entity.Post
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val authorId: Long,
    val title: String,
    val content: String,
    val place: String,
    val price: Long,
    val saleStatus: Boolean,
    val auctionStatus: Boolean,
    val likes: Int,
    val reports: Int,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
    val isAuthor: Boolean
) {
    companion object {
        fun from(post: Post, memberId: Long): PostDto {
            val isAuthor = post.memberId == memberId

            return PostDto(
                id = post.id!!,
                authorId = post.memberId,
                title = post.title,
                content = post.content,
                place = post.place,
                price = post.price,
                saleStatus = post.saleStatus,
                auctionStatus = post.auctionStatus,
                likes = post.likes,
                reports = post.reports,
                createdAt = post.createdAt,
                modifiedAt = post.modifiedAt,
                isAuthor = isAuthor // 작성자 여부 설정
            )
        }
    }
}
