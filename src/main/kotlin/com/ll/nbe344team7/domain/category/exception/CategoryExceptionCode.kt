package com.ll.nbe344team7.domain.category.exception

import org.springframework.http.HttpStatus

enum class CategoryExceptionCode(val status: HttpStatus, val code: String, val message: String) {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "존재하지 않는 카테고리입니다.")

}