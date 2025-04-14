package com.ll.nbe344team7.domain.pay.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 24.
 */
@Schema(description = "충전 DTO")
data class DepositDTO(
    @field:Schema(description = "충전 금액", example = "50000")
    val price: Long?,
    @field:Schema(description = "UID", example = "imp-12345678")
    val impUid: String
){
    constructor() : this(0, "")
}