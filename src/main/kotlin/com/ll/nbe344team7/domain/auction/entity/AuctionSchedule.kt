package com.ll.nbe344team7.domain.auction.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author shjung
 * @since 25. 4. 4.
 */
@Entity
data class AuctionSchedule(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val auctionId: Long?,
    val closedTime: LocalDateTime,
    var executed: Boolean = false
    ){
    constructor(auction: Auction) : this(
        id = null,
        auctionId = auction.id,
        closedTime = auction.closedAt,
        executed = false
    )

    fun updateExecuted(isExecuted: Boolean) {
        this.executed = isExecuted
    }
}
