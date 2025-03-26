package com.ll.nbe344team7.domain.chat.message.repository;

import com.ll.nbe344team7.domain.chat.message.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jyson
 * @since 25. 3. 25.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoomId(Pageable pageable, long id);

    Page<ChatMessage> findByChatRoomIdAndContentContaining(Pageable pageable, long id, String message);
}
