package com.ll.nbe344team7.domain.chat.message.entity

import com.ll.nbe344team7.domain.chat.room.entity.ChatRoom
import com.ll.nbe344team7.domain.member.entity.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_message")
class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var member: Member

    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var chatRoom: ChatRoom

    @Column(columnDefinition = "TEXT")
    lateinit var content: String

    lateinit var createdAt: LocalDateTime

    constructor(member: Member, content: String, chatRoom: ChatRoom, createdAt: LocalDateTime) {
        this.member = member
        this.content = content
        this.chatRoom = chatRoom
        this.createdAt = createdAt
    }
}