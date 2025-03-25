package com.ll.nbe344team7.domain.chatparticipant.entity

import com.ll.nbe344team7.domain.chatroom.entity.ChatRoom
import com.ll.nbe344team7.domain.member.Member
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*

/**
 *
 *
 * @author kjm72
 * @since 2025-03-25
 */
@Entity
class ChatParticipant(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    val chatroom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}