package com.ll.nbe344team7.domain.account.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 24.
 */
@Schema(description = "거래 내역 DTO")
data class ExchangeDTO (
    @field:Schema(description = "거래 타입", example = "1")
    val exchangeType: String,
    @field:Schema(description = "거래 일자", example = "2025-04-11T02:44:42.524Z")
    val payDate: String,
    @field:Schema(description = "금액", example = "20000")
    val price: Long,
    @field:Schema(description = "계좌", example = "123-456-7890")
    val account: Long,
    @field:Schema(description = "상대 유저 닉네임", example = "nickname2")
    val otherName: String,
    @field:Schema(description = "거래 후 잔액", example = "20000")
    val totalPrice: Long,
)