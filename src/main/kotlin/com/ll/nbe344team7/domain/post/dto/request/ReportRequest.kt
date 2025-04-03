package com.ll.nbe344team7.domain.post.dto.request

data class ReportRequest(
    val title: String = "",
    val content: String = "",
    val type: Int = 1 // 1: 사기 , 2: 욕설
)