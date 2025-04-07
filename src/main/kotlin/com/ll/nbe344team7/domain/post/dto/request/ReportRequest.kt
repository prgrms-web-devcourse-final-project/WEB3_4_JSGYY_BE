package com.ll.nbe344team7.domain.post.dto.request

import com.ll.nbe344team7.domain.post.entity.ReportType

data class ReportRequest(
    val title: String = "",
    val content: String = "",
    val type: ReportType = ReportType.ETC
)