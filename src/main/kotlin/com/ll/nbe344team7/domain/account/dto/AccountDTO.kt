package com.ll.nbe344team7.domain.account.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * 보유금 돌려받기 위한 은행 이름 및 계좌번호 입력
 *
 * @author shjung
 * @since 25. 3. 31.
 */
@Schema(description = "계좌 DTO")
data class AccountDTO (
    @field:Schema(description = "계좌 번호", required = true, example = "111-11-11111")
    var accountNumber: String,
    @field:Schema(description = "은행 이름", required = true, example = "신한은행")
    val bankName: String
){
    constructor(): this("","")
}