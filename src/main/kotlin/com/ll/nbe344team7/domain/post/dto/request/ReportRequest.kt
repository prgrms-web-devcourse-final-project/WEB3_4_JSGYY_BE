package com.ll.nbe344team7.domain.post.dto.request

import com.ll.nbe344team7.domain.post.entity.ReportType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "게시글 신고 DTO")
data class ReportRequest(
    @field:Schema(description = "신고 제목", example = "사기꾼 잡아주세요")
    val title: String = "",
    @field:Schema(description = "신고 내용", example = "사기를 당했어요")
    val content: String = "",
    @field:Schema(description = "신고 유형", example = "기타")
    val type: ReportType = ReportType.ETC
){
    constructor(): this("","",ReportType.ETC)
}