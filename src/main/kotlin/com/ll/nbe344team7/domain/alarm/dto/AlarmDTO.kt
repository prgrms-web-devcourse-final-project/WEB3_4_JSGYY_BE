package com.ll.nbe344team7.domain.alarm.dto

import com.ll.nbe344team7.domain.alarm.entity.Alarm
import com.ll.nbe344team7.domain.member.entity.Member
import software.amazon.awssdk.services.s3.model.Destination
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
data class AlarmDTO(
    val id : Long?,
    val receiveMemberId : Long,
    val content: String,
    val type: Int,
    val checked: Boolean?,
    val createdAt: LocalDateTime?,
    val destinationId : Long)
{
    constructor(dto: Alarm) :this(
        id = dto.id,
        receiveMemberId = dto.member.id!!,
        content = dto.content,
        type=dto.type,
        checked = dto.checked,
        createdAt = dto.createdAt,
        destinationId = dto.destinationId
    )
    constructor() : this(
        id = null,
        receiveMemberId = 0L,
        content = "",
        type = 0,
        checked = false,
        createdAt = LocalDateTime.now(),
        destinationId= 1
    )
}