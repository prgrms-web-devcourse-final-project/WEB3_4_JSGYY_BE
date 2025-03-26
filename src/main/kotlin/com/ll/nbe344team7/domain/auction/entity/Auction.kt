package com.ll.nbe344team7.domain.auction.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    val postId: Long,
    var maxPrice: Long,
    val startedAt: LocalDateTime,
    val closedAt: LocalDateTime,
    var status: Int,
    var memberId: Long)