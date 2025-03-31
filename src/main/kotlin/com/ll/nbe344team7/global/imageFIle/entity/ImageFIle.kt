package com.ll.nbe344team7.global.imageFIle.entity

import jakarta.persistence.*

@Entity
data class ImageFile(
    var url: String, // 이미지 URL

    @Column(name = "post_id", nullable = false)
    var postId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
