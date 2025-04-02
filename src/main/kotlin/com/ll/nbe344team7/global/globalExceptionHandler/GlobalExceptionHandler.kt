package com.ll.nbe344team7.global.globalExceptionHandler


import com.ll.nbe344team7.domain.member.exception.MemberException
import com.ll.nbe344team7.domain.post.exception.PostException
import com.ll.nbe344team7.global.exception.GlobalException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import com.ll.nbe344team7.global.security.exception.SecurityException;

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

    @ExceptionHandler(MemberException::class)
    fun handleMemberException(ex: MemberException) : ResponseEntity<Map<String,Any?>>{
        val responseBody = mapOf(
        "status" to ex.status.value(),
            "code" to ex.code,
            "message" to ex.message
        )

        return ResponseEntity
            .status(ex.status)
            .body(responseBody)
    }

    @ExceptionHandler(SecurityException::class)
    fun handleSecurityException(ex: SecurityException) : ResponseEntity<Map<String,Any?>>{
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