package com.ll.nbe344team7.domain.member.repository;

import com.ll.nbe344team7.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * member repository
 * @author 이광석
 * @since 25.03.25
 */
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    MemberEntity findByUserName(String username);
}
