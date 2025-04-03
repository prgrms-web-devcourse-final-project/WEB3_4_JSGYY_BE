package com.ll.nbe344team7.global.imageFIle

import com.ll.nbe344team7.global.imageFIle.entity.ImageFile

data class ImageFileDto (
    val id: Long,
    val url: String
) {
    companion object {
        fun from(imageFile: ImageFile): ImageFileDto {
            return ImageFileDto(
                id = imageFile.id!!,
                url = imageFile.url
            )
        }
    }
}