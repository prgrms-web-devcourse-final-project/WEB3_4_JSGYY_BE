package com.ll.nbe344team7.domain.member.repository;

import com.ll.nbe344team7.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
}
