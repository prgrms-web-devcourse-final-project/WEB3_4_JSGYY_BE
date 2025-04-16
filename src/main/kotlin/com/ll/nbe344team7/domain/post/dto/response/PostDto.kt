package com.ll.nbe344team7.domain.post.dto.response

import com.ll.nbe344team7.domain.post.entity.Post
import com.ll.nbe344team7.global.imageFIle.ImageFileDto
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val authorId: Long,
    val authorUsername: String,
    val authorNickname: String,
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
    val isLiked: Boolean = false,
    val images: List<ImageFileDto>
) {
    companion object {
        fun from(post: Post, memberId: Long, isLiked: Boolean): PostDto {
            val isAuthor = post.member.id == memberId
            var maxPrice = post.price
            if(post.auctionStatus) maxPrice = post.auctionDetails?.maxPrice!!
            return PostDto(
                id = post.id!!,
                authorId = post.member.id!!,
                authorUsername = post.member.username,
                authorNickname = post.member.nickname,
                title = post.title,
                content = post.content,
                category = post.category,
                place = post.place,
                price = maxPrice,
                saleStatus = post.saleStatus,
                auctionStatus = post.auctionStatus,
                auctionStartedAt = post.auctionDetails?.startedAt,
                auctionClosedAt = post.auctionDetails?.closedAt,
                likes = post.likes,
                reports = post.reports,
                createdAt = post.createdAt,
                modifiedAt = post.modifiedAt,
                isAuthor = isAuthor, // 작성자 여부 설정
                isLiked = isLiked,
                images = post.images.map { ImageFileDto.from(it) }
            )
        }
    }
}
