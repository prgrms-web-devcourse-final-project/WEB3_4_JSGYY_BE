package com.ll.nbe344team7.domain.post.dto.request

data class PostRequest (
    val title: String = "",
    val content: String = "",
    val price: Long = 0L,
    val place: String = "",
    val saleStatus: Boolean = true,
    val auctionStatus: Boolean = false,
    val auctionRequest: AuctionRequest? = null
)