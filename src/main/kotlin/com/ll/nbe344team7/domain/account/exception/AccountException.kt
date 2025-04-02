package com.ll.nbe344team7.domain.account.exception

import org.springframework.http.HttpStatus

/**
 *
 *
 *
 * @author shjung
 * @since 25. 4. 1.
 */
class AccountException(accountExceptionCode: AccountExceptionCode): RuntimeException(accountExceptionCode.message) {
    val status: HttpStatus = accountExceptionCode.status
    val code: String = accountExceptionCode.code
}