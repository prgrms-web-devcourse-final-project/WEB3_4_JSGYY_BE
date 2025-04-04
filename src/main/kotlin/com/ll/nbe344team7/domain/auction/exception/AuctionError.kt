package com.ll.nbe344team7.domain.auction.exception

import org.springframework.http.HttpStatus

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 25.
 */
enum class AuctionError(val status: HttpStatus, val message: String, val code: String) {
    NOT_OVER_ACCOUNT(HttpStatus.NOT_FOUND, "NOT_OVER_ACCOUNT", "보유금보다 높은 입찰가를 입찰할 수는 없습니다."),
    NOT_FOUND_AUCTION(HttpStatus.NOT_FOUND, "AUCTION_NOT_FOUND", "보유금보다 높은 입찰가를 입찰할 수는 없습니다.")
}