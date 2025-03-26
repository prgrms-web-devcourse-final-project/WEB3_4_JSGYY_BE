package com.ll.nbe344team7.domain.post.exception

import org.springframework.http.HttpStatus

class PostException(val errorCode: PostErrorCode) : RuntimeException(errorCode.message) {
    val status: HttpStatus = errorCode.status
    val code: String = errorCode.code
}
