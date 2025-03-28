package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.auction.repository.AuctionRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.post.dto.request.AuctionRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostRequest;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.exception.PostErrorCode;
import com.ll.nbe344team7.domain.post.exception.PostException;
import com.ll.nbe344team7.domain.post.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = new Member(
                null,
                "testUsername",
                "testName",
                "testPassword",
                "testNickname",
                "test@email.com",
                "010-1234-5678",
                false,
                "ROLE_ADMIN"
        );
        memberRepository.save(member);
    }

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
        auctionRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("게시글 작성 성공")
    void t1() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "testPlace",
                true,
                false,
                null);



        // when
        postService.createPost(postRequest, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        // then
        assertThat(savedPost.isPresent()).isTrue();
        assertThat(savedPost.get().getTitle()).isEqualTo(postRequest.getTitle());
        assertThat(savedPost.get().getContent()).isEqualTo(postRequest.getContent());
        assertThat(savedPost.get().getPrice()).isEqualTo(postRequest.getPrice());
        assertThat(savedPost.get().getPlace()).isEqualTo(postRequest.getPlace());
    }

    @Test
    @DisplayName("게시글 작성 실패 - 제목이 없는 경우")
    void t2() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "",
                "testContent",
                1000,
                "testPlace",
                true,
                false,
                null);

        // when
        // then
        assertThatThrownBy(() -> postService.createPost(postRequest, member.getId()))
                .isInstanceOf(PostException.class)
                .hasMessageContaining(PostErrorCode.INVALID_TITLE.getMessage());
    }

    @Test
    @DisplayName("게시글 작성 실패 - 내용이 없는 경우")
    void t3() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "",
                1000,
                "testPlace",
                true,
                false,
                null);

        // when
        // then
        assertThatThrownBy(() -> postService.createPost(postRequest, member.getId()))
                .isInstanceOf(PostException.class)
                .hasMessageContaining(PostErrorCode.INVALID_CONTENT.getMessage());
    }

    @Test
    @DisplayName("게시글 작성 실패 - 가격이 올바르지 않는 경우")
    void t4() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                -1000,
                "testPlace",
                true,
                false,
                null);

        // when
        // then
        assertThatThrownBy(() -> postService.createPost(postRequest, member.getId()))
                .isInstanceOf(PostException.class)
                .hasMessageContaining(PostErrorCode.INVALID_PRICE.getMessage());
    }

    @Test
    @DisplayName("게시글 작성 실패 - 장소가 없는 경우")
    void t5() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "",
                true,
                false,
                null);

        // when
        // then
        assertThatThrownBy(() -> postService.createPost(postRequest, member.getId()))
                .isInstanceOf(PostException.class)
                .hasMessageContaining(PostErrorCode.INVALID_PLACE.getMessage());
    }

    @Test
    @DisplayName("경매 게시글 작성")
    void t6() throws Exception {
        // given
        AuctionRequest auctionRequest = new AuctionRequest(
                LocalDateTime.of(2025, 3, 27, 10, 0, 0, 0),
                LocalDateTime.of(2025, 4, 10, 10, 0, 0, 0)
        );

        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "testPlace",
                true,
                true,
                auctionRequest);

        // when
        postService.createPost(postRequest, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        // then
        assertThat(savedPost.isPresent()).isTrue();
        assertThat(savedPost.get().getAuctionDetails().getStartedAt()).isEqualTo(auctionRequest.getStartedAt());
        assertThat(savedPost.get().getAuctionDetails().getClosedAt()).isEqualTo(auctionRequest.getClosedAt());

    }
}
