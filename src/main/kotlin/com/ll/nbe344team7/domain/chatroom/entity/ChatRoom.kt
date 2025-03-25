package com.ll.nbe344team7.domain.chatroom.entity

import com.ll.nbe344team7.global.base.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ChatRoom : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = 0

    companion object {
        fun create(): ChatRoom {
            return ChatRoom()
        }
    }
}