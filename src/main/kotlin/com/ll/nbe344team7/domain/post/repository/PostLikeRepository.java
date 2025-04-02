package com.ll.nbe344team7.domain.post.repository;

import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    Boolean existsByPostIdAndMemberId(Long postId, Long memberId);
}
