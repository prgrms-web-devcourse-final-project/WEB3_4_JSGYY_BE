package com.ll.nbe344team7.domain.post.exception

import org.springframework.http.HttpStatus

class PostException(val errorCode: PostErrorCode) : RuntimeException(errorCode.message) {
    fun getErrorCode(): String {
        return errorCode.code
    }

    fun getHttpStatus(): HttpStatus {
        return errorCode.status
    }
}
