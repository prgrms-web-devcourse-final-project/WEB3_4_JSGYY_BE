package com.ll.nbe344team7.domain.post.dto

data class PostRequest (
    val title: String,
    val content: String,
    val price: Long,
    val saleStatus: Boolean = true,
    val place: String,
    val auctionStatus: Boolean,
    val auctionRequest: AuctionRequest?
)