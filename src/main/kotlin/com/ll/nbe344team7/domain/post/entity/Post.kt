package com.ll.nbe344team7.domain.post.entity

import com.ll.nbe344team7.domain.auction.entity.Auction
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Post (
    memberId: Long,
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
    var memberId: Long = memberId
        protected set

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

    var likes: Int = 0
        protected set

    var reports: Int = 0
        protected set

    fun update(title: String, content: String, price: Long, place: String, saleStatus: Boolean, auctionStatus: Boolean) {
        this.title = title
        this.content = content
        this.price = price
        this.place = place
        this.saleStatus = saleStatus
        this.auctionStatus = auctionStatus
    }

    @OneToOne(mappedBy = "post", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    var auctionDetails: Auction? = null
        protected set

    fun updateAuctionStatus(status: Boolean) {
        this.auctionStatus = status
    }

    fun createAuction(startedAt: LocalDateTime, closedAt: LocalDateTime): Auction {
        return Auction(
            startedAt = startedAt,
            closedAt = closedAt,
            startPrice = this.price,  // Post의 price를 Auction의 startPrice로 사용
            status = 0,  // 경매 진행 상태 (0 = 진행 중)
        ).apply {
            this.post = this@Post  // Post와 Auction 관계 설정
        }
    }
}