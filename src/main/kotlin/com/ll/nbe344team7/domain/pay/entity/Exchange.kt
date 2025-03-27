package com.ll.nbe344team7.domain.pay.entity

import com.ll.nbe344team7.domain.pay.dto.DepositDTO
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
data class Exchange(
    @Id @GeneratedValue val id: Long? = null,
    val myId: Long?,
    val otherId: Long?,
    val payDate: LocalDateTime,
    val price: Long?,
    var status: Int,
    val exchangeType: Int,
    val impUid: String
){
    constructor(dto: DepositDTO) : this(
        id = null,
        myId = dto.memberId,
        otherId = dto.memberId,
        payDate = LocalDateTime.now(),
        price = dto.price,
        status = 1,
        exchangeType = 0,
        impUid = dto.impUid
    )
}