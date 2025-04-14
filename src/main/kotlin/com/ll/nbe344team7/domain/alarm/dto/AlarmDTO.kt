package com.ll.nbe344team7.domain.alarm.dto

import com.ll.nbe344team7.domain.alarm.entity.Alarm
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *
 *
 *
 * @author jyson
 * @since 25. 3. 24.
 */
@Schema(description = "알람 DTO")
data class AlarmDTO(
    @field:Schema(description = "알람 ID", example = "1")
    val id : Long?,
    @field:Schema(description = "받는 사용자 ID", example = "1")
    val receiveMemberId : Long,
    @field:Schema(description = "알람 내용", example = "낙찰되었습니다.")
    val content: String,
    @field:Schema(description = "알림 타입", example = "1")
    val type: Int,
    @field:Schema(description = "알림 확인", example = "false")
    val checked: Boolean?,
    @field:Schema(description = "생성 날짜" , example = "")
    val createdAt: LocalDateTime?,
    val destinationId : Long)
{
    constructor(alarm: Alarm) :this(
        id = alarm.id,
        receiveMemberId = alarm.member.id!!,
        content = alarm.content,
        type=alarm.type,
        checked = alarm.checked,
        createdAt = alarm.createdAt,
        destinationId = alarm.destinationId
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