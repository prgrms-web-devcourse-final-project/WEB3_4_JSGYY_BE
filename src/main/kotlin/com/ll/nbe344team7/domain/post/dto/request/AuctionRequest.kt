package com.ll.nbe344team7.domain.post.dto.request

import java.time.LocalDateTime

data class AuctionRequest (
    val startedAt: LocalDateTime,
    val closedAt: LocalDateTime
)