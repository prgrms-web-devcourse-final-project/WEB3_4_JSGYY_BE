package com.ll.nbe344team7.domain.follow.repository

import com.ll.nbe344team7.domain.follow.entity.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 *
 *
 * @author kjm72
 * @since 2025-04-04
 */
@Repository
interface FollowRepository : JpaRepository<Follow, Long>{

}
