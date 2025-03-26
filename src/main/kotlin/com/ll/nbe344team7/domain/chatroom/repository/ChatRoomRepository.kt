package com.ll.nbe344team7.domain.chatroom.repository;

import com.ll.nbe344team7.domain.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author kjm72
 * @since 2025-03-26
 */
@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
}
