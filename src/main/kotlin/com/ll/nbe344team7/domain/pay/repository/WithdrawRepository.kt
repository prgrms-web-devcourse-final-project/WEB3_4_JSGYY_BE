package com.ll.nbe344team7.domain.pay.repository

import com.ll.nbe344team7.domain.pay.entity.Withdraw
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 *
 *
 *
 * @author shjung
 * @since 25. 4. 1.
 */
@Repository
interface WithdrawRepository: JpaRepository<Withdraw, Long> {
}