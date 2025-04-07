package com.ll.nbe344team7.domain.post.entity

enum class ReportType(val code: Int, val description: String) {
    FRAUD(1, "사기"),
    ABUSE(2, "욕설"),
    ETC(3, "기타");

    companion object {
        fun fromCode(code: Int): ReportType {
            return entries.firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Invalid report type code: $code")
        }
    }
}