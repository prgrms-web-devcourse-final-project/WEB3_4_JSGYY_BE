package com.ll.nbe344team7.domain.follow.entity

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

    @Column(name = "member_id", nullable = false)
    val userId : Long? = null,

    @Column(name = "following_id", nullable = false)
    val followingId : Long? = null
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null
}
