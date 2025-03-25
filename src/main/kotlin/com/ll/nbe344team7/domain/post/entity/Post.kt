package com.ll.nbe344team7.domain.post.entity

import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*

@Entity
class Post (
    title: String,
    content: String,
    price: Long,
    place: String,
    auctionStatus: Boolean
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var title: String = title
        protected set

    @Column(nullable = false)
    var content: String = content
        protected set

    @Column(nullable = false)
    var price: Long = price
        protected set

    @Column(nullable = false)
    var place: String = place
        protected set

    @Column(nullable = false)
    var saleStatus: Boolean = true
        protected set

    @Column(nullable = false)
    var auctionStatus: Boolean = auctionStatus
        protected set

    var likes: Long = 0L
        protected set

    var reports: Long = 0L
        protected set
}