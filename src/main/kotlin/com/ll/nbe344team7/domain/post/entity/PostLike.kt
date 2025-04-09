package com.ll.nbe344team7.domain.post.entity

import com.ll.nbe344team7.domain.member.entity.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class PostLike (
    member: Member,
    post: Post
) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member = member
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post = post
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set
}