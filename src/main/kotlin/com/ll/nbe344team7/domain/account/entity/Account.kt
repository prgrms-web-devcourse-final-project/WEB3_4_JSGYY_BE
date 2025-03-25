package com.ll.nbe344team7.domain.account.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

/**
 *
 *
 *
 * @author shjung
 * @since 25. 3. 24.
 */
@Entity
data class Account (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long,
    val memberId: Long,
    var money: Long,
    var accountNumber: String,
    var bankName: String)