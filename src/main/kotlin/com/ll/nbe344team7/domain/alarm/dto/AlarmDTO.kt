package com.ll.nbe344team7.domain.alarm.dto

import java.time.LocalDateTime

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
data class AlarmDTO(val content: String, val type: String, val isCheck: Boolean, val createdAt: LocalDateTime)