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
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "멤버를 조회할 수 없습니다."),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "게시글을 조회할 수 없습니다."),
    NOT_OVER_ACCOUNT(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "게시글을 조회할 수 없습니다.")
}