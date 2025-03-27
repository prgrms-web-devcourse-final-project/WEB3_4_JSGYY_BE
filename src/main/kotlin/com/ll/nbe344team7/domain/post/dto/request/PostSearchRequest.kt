package com.ll.nbe344team7.domain.post.dto.request

data class PostSearchRequest(
    val minPrice: Long? = null,
    val maxPrice: Long? = null,
    val saleStatus: Boolean? = null,
    val keyword: String? = null
)