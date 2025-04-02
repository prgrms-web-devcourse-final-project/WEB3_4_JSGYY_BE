package com.ll.nbe344team7.domain.account.exception

import org.springframework.http.HttpStatus

/**
 *
 *
 *
 * @author shjung
 * @since 25. 4. 1.
 */
enum class AccountExceptionCode (val status: HttpStatus, val code: String, val message: String){
    NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, "404", "계좌를 찾을 수 없습니다."),
    NOT_FOUND_EXCHANGE(HttpStatus.NOT_FOUND, "404", "거래 내역을 찾을 수 없습니다."),
    NOT_TYPE_EXCHANGE(HttpStatus.BAD_REQUEST, "400", "조회 타입이 올바르지 않습니다..")
}