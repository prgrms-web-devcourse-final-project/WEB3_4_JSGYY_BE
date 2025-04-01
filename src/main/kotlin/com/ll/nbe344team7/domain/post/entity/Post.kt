package com.ll.nbe344team7.domain.post.entity

import com.ll.nbe344team7.domain.auction.entity.Auction
import com.ll.nbe344team7.domain.member.entity.Member
import com.ll.nbe344team7.global.base.BaseEntity
import com.ll.nbe344team7.global.imageFIle.entity.ImageFile
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Post (
    member: Member,
    title: String,
    content: String,
    price: Long,
    place: String,
    auctionStatus: Boolean
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member = member
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

    @OneToMany(mappedBy = "post", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var images: MutableList<ImageFile> = mutableListOf()
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

    fun like() {
        this.likes++ // 좋아요 수 증가
    }

    fun unlike() {
        this.likes-- // 좋아요 수 감소
    }
}