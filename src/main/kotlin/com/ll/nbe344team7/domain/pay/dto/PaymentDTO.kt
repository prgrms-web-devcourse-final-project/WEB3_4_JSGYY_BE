package com.ll.nbe344team7.domain.pay.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 *
 * @author shjung
 * @since 25. 4. 2.
 */
@Schema(description = "결제 DTO")
data class PaymentDTO (
    @field:Schema(description = "게시글 ID", required = true, example = "1")
    val postId: Long
){
    constructor() : this(0L)
}