package com.ll.nbe344team7.domain.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "게시글 작성 DTO")
data class PostRequest (
    @field:Schema(description = "제목", required = true, example = "바지")
    val title: String = "",
    @field:Schema(description = "내용", required = true, example = "청바지")
    val content: String = "",
    @field:Schema(description = "가격", required = true, example = "20000")
    val price: Long = 0L,
    @field:Schema(description = "카테고리", required = true, example = "남성의류")
    val category: String = "",
    @field:Schema(description = "장소", required = true, example = "서울시 관악구")
    val place: String = "",
    @field:Schema(description = "판매 상태", example = "true")
    val saleStatus: Boolean = true,
    @field:Schema(description = "경매 여부", example = "false")
    val auctionStatus: Boolean = false,
    @field:Schema(description = "경매 시작 시간과 종료시간(경매 여부가 true일 경우 작성 아니면 null로 입력)", example = "\"startedAt\":\"2025-03-27T10:00:00\",\"closedAt\":\"2025-04-10T10:00:00\"")
    val auctionRequest: AuctionRequest? = null
)