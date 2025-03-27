package com.ll.nbe344team7.domain.chat.message.entity

import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom
import com.ll.nbe344team7.domain.member.entity.MemberEntity
import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "chat_message")
class ChatMessage : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var member: MemberEntity

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var chatRoom: ChatRoom

    @Column(columnDefinition = "TEXT")
    lateinit var content: String

    constructor(member: MemberEntity, content: String, chatRoom: ChatRoom) {
        this.member = member
        this.content = content
        this.chatRoom = chatRoom
    }
}