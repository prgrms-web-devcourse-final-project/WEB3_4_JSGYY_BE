package com.ll.nbe344team7.domain.post.entity

import com.ll.nbe344team7.domain.member.entity.Member
import jakarta.persistence.*

@Entity
class Report(
    member: Member,
    post: Post,
    title: String,
    content: String,
    type: Int // 1: 사기 , 2: 욕설
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member = member
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post = post
        protected set

    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(nullable = false)
    var content: String = content
        protected set

    @Column(nullable = false)
    var type: Int = type
        protected set
}