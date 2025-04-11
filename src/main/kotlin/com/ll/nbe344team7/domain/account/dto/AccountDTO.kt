package com.ll.nbe344team7.domain.account.dto

/**
 *
 * 보유금 돌려받기 위한 은행 이름 및 계좌번호 입력
 *
 * @author shjung
 * @since 25. 3. 31.
 */
data class AccountDTO (val accountNumber: String, val bankName: String){
    constructor(): this("","")
}