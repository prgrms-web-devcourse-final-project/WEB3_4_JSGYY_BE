package com.ll.nbe344team7.domain.member.repository;

import com.ll.nbe344team7.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

import java.util.Optional;



/**
 * member repository
 * @author 이광석
 * @since 25.03.25
 */
public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByUsername(String username);

    Member findByNickname(String nickname);


    List<Member> findAllByIdIn(List<Long> followingIds);

    Optional<Member> findFirstByOrderByIdDesc();

}
