package com.ll.nbe344team7.domain.chatparticipants.entity

import com.ll.nbe344team7.domain.chatroom.entity.ChatRoom
import com.ll.nbe344team7.domain.member.Member
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*

@Entity
class ChatParticipants(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = 0

    companion object {
        fun create(chatRoom: ChatRoom, member: Member): ChatParticipants {
            return ChatParticipants(chatRoom = chatRoom, member = member)
        }
    }
}