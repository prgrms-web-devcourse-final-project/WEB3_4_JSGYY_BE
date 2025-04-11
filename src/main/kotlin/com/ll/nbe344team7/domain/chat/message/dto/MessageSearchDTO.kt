package com.ll.nbe344team7.domain.chat.message.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
@Schema(description = "메시지 검색 및 페이징 요청 DTO")
data class MessageSearchDTO(
    @field:Schema(description = "메시지 검색어", example = "안녕하세요")
    var message: String,
    @field:Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    var page: Int,
    @field:Schema(description = "페이지 크기", example = "10")
    var size: Int){
    constructor():this("",0,10)
}