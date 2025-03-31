package com.ll.nbe344team7.global.imageFIle.exception

import org.springframework.http.HttpStatus

class S3Exception(val errorCode: S3ExceptionCode) : RuntimeException(errorCode.message) {
    val status: HttpStatus = errorCode.status
    val code: String = errorCode.code
}