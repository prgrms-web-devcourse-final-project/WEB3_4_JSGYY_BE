package com.ll.nbe344team7.domain.post.dto.request

data class PostRequest (
    val title: String,
    val content: String,
    val price: Long,
    val place: String,
    val saleStatus: Boolean = true,
    val auctionStatus: Boolean,
    val auctionRequest: AuctionRequest?
)