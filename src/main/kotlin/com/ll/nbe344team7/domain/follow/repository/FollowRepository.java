package com.ll.nbe344team7.domain.follow.repository;

import com.ll.nbe344team7.domain.follow.entity.Follow;
import com.ll.nbe344team7.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByUserAndFollowing(Member user, Member following);
    Follow findByUserAndFollowing(Member user, Member following);
}
