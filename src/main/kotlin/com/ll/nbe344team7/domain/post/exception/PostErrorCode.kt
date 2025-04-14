package com.ll.nbe344team7.domain.post.exception

import org.springframework.http.HttpStatus

enum class PostErrorCode(val status: HttpStatus, val code: String, val message: String) {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "존재하지 않는 게시글입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACCESS", "게시글 권한이 없습니다."),
    INVALID_TITLE(HttpStatus.BAD_REQUEST, "INVALID_TITLE", "제목은 최소 1자, 최대 50자로 입력해주세요."),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST, "INVALID_CONTENT","내용은 최소 1자, 최대 500자로 입력해주세요."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "INVALID_PRICE", "가격은 0원 이상으로 입력해주세요."),
    INVALID_PLACE(HttpStatus.BAD_REQUEST, "INVALID_PLACE", "장소를 입력해주세요."),
    INVALID_AUCTION_DATE(HttpStatus.BAD_REQUEST, "INVALID_AUCTION_DATE", "경매 종료일은 경매 시작일 이후로 입력해주세요."),
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_LIKE_NOT_FOUND", "존재하지 않는 좋아요입니다."),
    INVALID_IMAGE_COUNT(HttpStatus.BAD_REQUEST, "INVALID_IMAGE_COUNT", "이미지는 최대 10개까지 업로드 가능합니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "ALREADY_LIKED", "이미 좋아요를 눌렀습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE_NOT_FOUND", "존재하지 않는 이미지입니다."),
    IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "IMAGE_REQUIRED", "게시글에는 최소 1장의 이미지가 있어야 합니다."),
    INVALID_REPORT_TITLE(HttpStatus.BAD_REQUEST, "INVALID_REPORT_TITLE", "신고 제목은 최소 1자, 최대 30자로 입력해주세요."),
    INVALID_REPORT_CONTENT(HttpStatus.BAD_REQUEST, "INVALID_REPORT_CONTENT", "신고 내용은 최소 1자, 최대 100자로 입력해주세요."),
    CANNOT_MODIFY_AUCTION(HttpStatus.BAD_REQUEST, "CANNOT_MODIFY_AUCTION", "경매글은 일반 게시글로 전환이 불가능합니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "INVALID_CATEGORY", "카테고리를 선택해주세요."),
}