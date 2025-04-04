package com.ll.nbe344team7.domain.auction.repository

import com.ll.nbe344team7.domain.auction.entity.AuctionSchedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author shjung
 * @since 25. 4. 4.
 */
@Repository
interface AuctionScheduleRepository: JpaRepository<AuctionSchedule, Long> {
    fun findByExecutedIsFalseAndClosedTimeBefore(closedTimeBefore: LocalDateTime): MutableList<AuctionSchedule>
}