package com.ll.nbe344team7.domain.pay.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 25.
 */
@Entity
data class Payment(
    @Id @GeneratedValue val id: Long,
    val myId: Long,
    val otherId: Long,
    val payDate: LocalDateTime,
    val price: Long,
    var status: Int,
    val exchangeType: Int
)