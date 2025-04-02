package com.ll.nbe344team7.global.imageFIle.exception

import org.springframework.http.HttpStatus

enum class S3ExceptionCode(val status: HttpStatus, val code: String, val message: String) {
    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "EMPTY_FILE_EXCEPTION", "파일이 비어있습니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "IO_EXCEPTION_ON_IMAGE_UPLOAD", "이미지 업로드 중 IO 예외가 발생했습니다."),
    NO_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "NO_FILE_EXTENTION", "파일 확장자가 없습니다."),
    INVALID_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "INVALID_FILE_EXTENTION", "유효하지 않은 파일 확장자입니다."),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "PUT_OBJECT_EXCEPTION", "S3에 객체를 업로드하는 중 예외가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "IO_EXCEPTION_ON_IMAGE_DELETE", "이미지 삭제 중 IO 예외가 발생했습니다."),

}