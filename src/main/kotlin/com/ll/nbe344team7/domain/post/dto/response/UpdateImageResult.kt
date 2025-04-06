package com.ll.nbe344team7.domain.post.dto.response

import com.ll.nbe344team7.global.imageFIle.ImageFileDto

data class UpdateImageResult(
    val uploadedImages: List<ImageFileDto>,
    val deletedImageIds: List<Long>
)