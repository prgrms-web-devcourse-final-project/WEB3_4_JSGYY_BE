package com.ll.nbe344team7.domain.auction.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 24.
 */
@Schema(description = "입찰 DTO")
data class BidDTO(
    @field:Schema(description = "가격", example = "20000")
    val price: Long = 0)