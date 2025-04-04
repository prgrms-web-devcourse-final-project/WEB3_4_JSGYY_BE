package com.ll.nbe344team7.domain.follow.entity

import com.ll.nbe344team7.domain.member.entity.Member
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*

/**
 *
 *
 * @author kjm72
 * @since 2025-04-04
 */
@Entity
class Follow(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val user : Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    val following : Member
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null
}
