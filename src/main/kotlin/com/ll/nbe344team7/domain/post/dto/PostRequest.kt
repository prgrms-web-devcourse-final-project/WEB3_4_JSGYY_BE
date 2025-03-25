package com.ll.nbe344team7.domain.post.dto

data class PostRequest (
    val title: String,
    val content: String,
    val place: String,
    val price: Int,
    val auctionStatus: Boolean
)