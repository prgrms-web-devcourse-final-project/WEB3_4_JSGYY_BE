package com.ll.nbe344team7.domain.chat.participant.repository;

import com.ll.nbe344team7.domain.chat.participant.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author kjm72
 * @since 2025-03-25
 */
@Repository
interface ChatParticipantRepository : JpaRepository<ChatParticipant, Long> {
    fun findByMemberId(memberId : Long): List<ChatParticipant>
}
