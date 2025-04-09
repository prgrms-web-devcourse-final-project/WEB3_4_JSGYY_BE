package com.ll.nbe344team7.domain.post.repository;

import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.entity.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    Boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    void deleteByPostId(Long postId);

    @Query("SELECT pl.post FROM PostLike pl WHERE pl.member.id = :memberId")
    Page<Post> findLikedPostsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}