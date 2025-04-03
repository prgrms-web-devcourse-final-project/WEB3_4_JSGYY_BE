package com.ll.nbe344team7.domain.alarm.dto

import com.ll.nbe344team7.domain.member.entity.Member
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
data class AlarmDTO(
    val memberId: Long,
    val username: String,
    val content: String,
    val type: Int,
    val isCheck: Boolean,
    val createdAt: LocalDateTime)