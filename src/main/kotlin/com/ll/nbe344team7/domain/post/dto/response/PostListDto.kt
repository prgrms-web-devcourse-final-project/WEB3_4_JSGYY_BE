package com.ll.nbe344team7.domain.post.dto.response

import com.ll.nbe344team7.domain.post.entity.Post
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "게시글 목록 리스트 DTO")
data class PostListDto(
    @field:Schema(description = " 게시글 ID", example = "1")
    val id: Long?,
    @field:Schema(description = " 게시글 제목", example = "바지")
    val title: String,
    @field:Schema(description = " 장소", example = "서울시 관악구")
    val place: String,
    @field:Schema(description = " 가격", example = "20000")
    val price: Long,
    @field:Schema(description = " 거래 상태", example = "true")
    val saleStatus: Boolean,
    @field:Schema(description = " 경매 여부", example = "false")
    val auctionStatus: Boolean,
    @field:Schema(description = " 썸네일", example = "https://example.com/image.jpg")
    val thumbnail: String?,
    @field:Schema(description = "작성일자", example = "2025-04-12T15:30:00")
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(post: Post): PostListDto {
            var maxPrice = post.price
            if(post.auctionStatus) maxPrice = post.auctionDetails?.maxPrice!!
            return PostListDto(
                id = post.id,
                title = post.title,
                place = post.place,
                price = maxPrice,
                saleStatus = post.saleStatus,
                auctionStatus = post.auctionStatus,
                thumbnail = post.images.firstOrNull()?.url,
                createdAt = post.createdAt
            )
        }
    }
}