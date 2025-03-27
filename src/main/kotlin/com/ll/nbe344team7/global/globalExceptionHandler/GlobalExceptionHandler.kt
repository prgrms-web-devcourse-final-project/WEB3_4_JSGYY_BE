package com.ll.nbe344team7.global.globalExceptionHandler


import com.ll.nbe344team7.domain.post.exception.PostException
import com.ll.nbe344team7.global.exception.GlobalException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException::class)
    fun handle(ex: GlobalException): ResponseEntity<Map<String, Any?>> {
        val responseBody = mapOf(
            "status" to ex.status.value(),
            "code" to ex.code,
            "message" to ex.message
        )

        return ResponseEntity
            .status(ex.status)
            .body(responseBody)
    }

    @ExceptionHandler(PostException::class)
    fun handlePostException(ex: PostException): ResponseEntity<Map<String, Any?>> {
        val responseBody = mapOf(
            "status" to ex.status.value(),
            "code" to ex.code,
            "message" to ex.message
        )

        return ResponseEntity
            .status(ex.status)
            .body(responseBody)
    }
}