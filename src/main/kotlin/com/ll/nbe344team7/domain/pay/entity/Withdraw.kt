package com.ll.nbe344team7.domain.pay.entity

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
 * @since 25. 4. 1.
 */
@Entity
data class Withdraw (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    val price: Long,
    val bankName: String,
    val accountNumber: String,
    val createdAt: LocalDateTime){
}