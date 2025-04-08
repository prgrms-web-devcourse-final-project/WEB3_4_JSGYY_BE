package com.ll.nbe344team7.domain.category.exception

import org.springframework.http.HttpStatus

class CategoryException(val errorCode: CategoryExceptionCode) : RuntimeException(errorCode.message) {
    val status: HttpStatus = errorCode.status
    val code: String = errorCode.code
}
