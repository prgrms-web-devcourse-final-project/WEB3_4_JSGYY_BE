package com.ll.nbe344team7.domain.follow.repository;

import com.ll.nbe344team7.domain.follow.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * @author kjm72
 * @since 2025-04-07
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserIdAndFollowingId(Long userId, Long followingId);
    Follow findByUserIdAndFollowingId(Long userId, Long followingId);
    Page<Follow> findAllByUserId(Long id, Pageable pageable);
}
