package com.ll.nbe344team7.domain.post.exception

import org.springframework.http.HttpStatus

enum class PostErrorCode(val status: HttpStatus, val code: String, val message: String) {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "존재하지 않는 게시글입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACCESS", "게시글 권한이 없습니다."),
    INVALID_TITLE(HttpStatus.BAD_REQUEST, "INVALID_TITLE", "제목은 최소 1자, 최대 50자로 입력해주세요."),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST, "INVALID_CONTENT","내용은 최소 1자, 최대 500자로 입력해주세요."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "INVALID_PRICE", "가격은 0원 이상으로 입력해주세요."),
    ALREADY_IN_AUCTION(HttpStatus.BAD_REQUEST, "ALREADY_IN_AUCTION", "이미 경매중인 게시글입니다."),
}
