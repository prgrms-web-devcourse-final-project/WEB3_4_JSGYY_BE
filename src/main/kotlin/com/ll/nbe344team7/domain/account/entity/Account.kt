package com.ll.nbe344team7.domain.account.entity

import com.ll.nbe344team7.domain.account.dto.AccountDTO
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val memberId: Long,
    var money: Long,
    var accountNumber: String,
    var bankName: String) {
    constructor(accountDTO: AccountDTO) : this(
        id= null,
        memberId = accountDTO.memberId,
        money = 0,
        accountNumber = accountDTO.accountNumber,
        bankName = accountDTO.bankName,
    )
    constructor(): this(0L, 0L, 0L, "", "")
}