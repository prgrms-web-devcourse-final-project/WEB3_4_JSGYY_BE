package com.ll.nbe344team7.domain.post.dto.request

data class PostSearchRequest(
    val category: String? = null,
    val minPrice: Long? = null,
    val maxPrice: Long? = null,
    val saleStatus: Boolean? = null,
    val keyword: String? = null,
    val place: String? = null // 시/구/동 단위
)