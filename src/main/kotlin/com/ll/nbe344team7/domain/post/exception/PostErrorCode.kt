package com.ll.nbe344team7.domain.post.exception

import org.springframework.http.HttpStatus

enum class PostErrorCode(val status: HttpStatus, val code: String, val message: String) {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "존재하지 않는 게시글입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACCESS", "게시글 권한이 없습니다.")
}
