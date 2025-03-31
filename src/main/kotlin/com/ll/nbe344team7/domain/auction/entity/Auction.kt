package com.ll.nbe344team7.domain.auction.entity

import com.ll.nbe344team7.domain.post.entity.Post
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *
 * 경매 Entity
 *
 * @author shjung
 * @since 25. 3. 25.
 */
@Entity
data class Auction (
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long? = null,
    var maxPrice: Long? = null,
    var startedAt: LocalDateTime,
    var closedAt: LocalDateTime,
    var startPrice: Long,
    var status: Int, // 0: 진행중, 1: 종료
    var winnerId: Long? = null // 낙찰자 ID
) {

    fun updateAuction(StartedAt: LocalDateTime, ClosedAt: LocalDateTime) {
        this.startedAt = StartedAt
        this.closedAt = ClosedAt
    }

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    var post: Post? = null
}