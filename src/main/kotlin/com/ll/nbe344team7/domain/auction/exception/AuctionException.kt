package com.ll.nbe344team7.domain.auction.exception

import org.springframework.http.HttpStatus

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 25.
 */
class AuctionException(auctionError: AuctionError) : RuntimeException(auctionError.message) {
    val status: HttpStatus = auctionError.status
    val code: String = auctionError.code
}