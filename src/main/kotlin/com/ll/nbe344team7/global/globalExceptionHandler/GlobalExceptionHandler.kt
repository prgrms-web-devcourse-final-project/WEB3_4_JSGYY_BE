package com.ll.nbe344team7.global.globalExceptionHandler


import com.ll.nbe344team7.domain.alarm.exception.AlarmException
import com.ll.nbe344team7.domain.member.exception.MemberException
import com.ll.nbe344team7.domain.post.exception.PostException
import com.ll.nbe344team7.global.exception.FollowException
import com.ll.nbe344team7.global.exception.GlobalException
import com.ll.nbe344team7.global.security.exception.SecurityException
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

    @ExceptionHandler(AlarmException::class)
    fun handleAlarmException(ex : AlarmException) : ResponseEntity<Map<String,Any?>>{
        val responseBody = mapOf(
            "status" to ex.status.value(),
            "code" to ex.code,
            "message" to ex.message
        )

        return ResponseEntity
            .status(ex.status)
            .body(responseBody)
    }

    @ExceptionHandler(FollowException::class)
    fun handleFollowException(ex: FollowException) : ResponseEntity<Map<String,Any?>>{
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