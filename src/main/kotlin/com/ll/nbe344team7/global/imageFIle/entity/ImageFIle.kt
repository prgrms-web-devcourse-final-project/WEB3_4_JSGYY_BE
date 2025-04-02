package com.ll.nbe344team7.global.imageFIle.entity

import com.ll.nbe344team7.domain.post.entity.Post
import jakarta.persistence.*

@Entity
data class ImageFile(
    var url: String // 이미지 URL
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post? = null
}
