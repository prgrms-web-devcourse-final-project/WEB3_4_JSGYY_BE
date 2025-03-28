package com.ll.nbe344team7.domain.pay.exception

import org.springframework.http.HttpStatus

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 28.
 */
enum class PayExceptionCode (val status: HttpStatus, val code: String, val message: String){
    PORTONE_ERROR(HttpStatus.FORBIDDEN, "403", "포트원에 연결할 수 없습니다."),
    PRICE_ERROR(HttpStatus.BAD_REQUEST, "400", "요청한 금액과 결제된 금액이 다릅니다."),
    PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "400", "이미 결제가 된 요청입니다."),
    PAYMENT_STATUS_ERROR(HttpStatus.BAD_REQUEST, "400", "결제 오류입니다. 다시 시도해주세요.")
}