package com.ll.nbe344team7.domain.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "경매 시간 DTO")
data class AuctionRequest (
    @field:Schema(description = "경매 시작 시간(auctionStatus가 true면 시간 값 입력)", example = "null")
    val startedAt: LocalDateTime = LocalDateTime.now(),
    @field:Schema(description = "경매 종료 시간(auctionStatus가 true면 시간 값 입력)", example = "null")
    val closedAt: LocalDateTime = LocalDateTime.now(),
){
    constructor(): this(LocalDateTime.now(), LocalDateTime.now())
}