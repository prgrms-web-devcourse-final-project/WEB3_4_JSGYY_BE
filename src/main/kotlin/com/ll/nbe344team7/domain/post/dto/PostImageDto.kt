package com.ll.nbe344team7.domain.post.dto

data class PostImageDto(
    val attachmentId: Long,
    val fileName: String,
    val filePath: String,
    val fileSize: Long
)
