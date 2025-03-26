package com.ll.nbe344team7.domain.chat.room.entity

import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*

/**
 *
 *
 * @author kjm72
 * @since 2025-03-25
 */
@Entity
class ChatRoom(
    var title : String,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @OneToMany(mappedBy = "chatroom", cascade = [CascadeType.ALL], orphanRemoval = true)
    var participants: MutableList<ChatParticipant> = mutableListOf()

    constructor() : this("채팅방") {
        this.participants = mutableListOf()
    }

    fun setTitle(userId: Long) {
        this.title = participants
            .map { it.member }
            .filter { it.id != userId }
            .map { it.nickName}
            .firstOrNull() ?: "알 수 없음"
    }

    fun addParticipant(participant: ChatParticipant) {
        this.participants.add(participant)
    }
}