package com.ll.nbe344team7.domain.pay.exception

import org.springframework.http.HttpStatus

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 28.
 */
class PaymentException(val payExceptionCode: PayExceptionCode) : RuntimeException(payExceptionCode.message){
    val status: HttpStatus = payExceptionCode.status
    val code: String = payExceptionCode.code
}