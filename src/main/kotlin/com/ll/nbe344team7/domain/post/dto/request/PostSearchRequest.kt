package com.ll.nbe344team7.domain.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "게시글 검색 DTO")
data class PostSearchRequest(
    @field:Schema(description = "카테고리", example = "남성의류")
    val category: String? = null,
    @field:Schema(description = "최소 가격(null => 전체 조회)", example = "null")
    val minPrice: Long? = null,
    @field:Schema(description = "최대 가격(null => 전체 조회)", example = "null")
    val maxPrice: Long? = null,
    @field:Schema(description = "판매상태", example = "true")
    val saleStatus: Boolean? = null,
    @field:Schema(description = "검색어(null => 전체 조회)", example = "null")
    val keyword: String? = null,
    @field:Schema(description = "장소(null => 전체 조회)", example = "null")
    val place: String? = null // 시/구/동 단위
)