package com.ll.nbe344team7.domain.pay.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 24.
 */
@Schema(description = "출금 DTO")
data class WithdrawDTO (
    @field:Schema(description = "출금 금액", required = true, example = "20000")
    val price: Long
){
    constructor() : this(0)
}