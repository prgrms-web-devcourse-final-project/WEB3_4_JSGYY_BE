package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.auction.repository.AuctionRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.post.dto.request.AuctionRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostSearchRequest;
import com.ll.nbe344team7.domain.post.dto.response.PostDto;
import com.ll.nbe344team7.domain.post.dto.response.PostListDto;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.exception.PostErrorCode;
import com.ll.nbe344team7.domain.post.exception.PostException;
import com.ll.nbe344team7.domain.post.repository.PostRepository;
import com.ll.nbe344team7.global.imageFIle.service.S3ImageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

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

    @MockitoBean
    private S3ImageService s3ImageService;

    private Member member;

    MultipartFile[] files = {
            new MockMultipartFile("file1", "test1.jpg", "image/jpeg", "file1-content".getBytes()),
            new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "file2-content".getBytes())
    };

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
        postService.createPost(postRequest, files, member.getId());

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
        assertThatThrownBy(() -> postService.createPost(postRequest, files, member.getId()))
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
        assertThatThrownBy(() -> postService.createPost(postRequest, files, member.getId()))
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
        assertThatThrownBy(() -> postService.createPost(postRequest, files, member.getId()))
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
        assertThatThrownBy(() -> postService.createPost(postRequest, files, member.getId()))
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
        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        // then
        assertThat(savedPost.isPresent()).isTrue();
        assertThat(savedPost.get().getAuctionDetails().getStartedAt()).isEqualTo(auctionRequest.getStartedAt());
        assertThat(savedPost.get().getAuctionDetails().getClosedAt()).isEqualTo(auctionRequest.getClosedAt());

    }

    @Test
    @DisplayName("경매 게시글 작성 실패 - 경매 시작 시간이 경매 종료 시간보다 늦은 경우")
    void t7() throws Exception {
        // given
        AuctionRequest auctionRequest = new AuctionRequest(
                LocalDateTime.of(2025, 4, 10, 10, 0, 0, 0),
                LocalDateTime.of(2025, 3, 27, 10, 0, 0, 0)
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
        // then
        assertThatThrownBy(() -> postService.createPost(postRequest, files, member.getId()))
                .isInstanceOf(PostException.class)
                .hasMessageContaining(PostErrorCode.INVALID_AUCTION_DATE.getMessage());
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void t8() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "testPlace",
                true,
                false,
                null);

        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        PostRequest updatePostRequest = new PostRequest(
                "updateTitle",
                "updateContent",
                2000,
                "updatePlace",
                false,
                false,
                null);

        // when
        postService.modifyPost(savedPost.get().getId(), updatePostRequest, files, member.getId());

        Optional<Post> updatedPost = postRepository.findById(savedPost.get().getId());

        // then
        assertThat(updatedPost.isPresent()).isTrue();
        assertThat(updatedPost.get().getTitle()).isEqualTo(updatePostRequest.getTitle());
        assertThat(updatedPost.get().getContent()).isEqualTo(updatePostRequest.getContent());
        assertThat(updatedPost.get().getPrice()).isEqualTo(updatePostRequest.getPrice());
        assertThat(updatedPost.get().getPlace()).isEqualTo(updatePostRequest.getPlace());
    }

    @Test
    @DisplayName("경매글 수정 성공")
    void t9() throws Exception {
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

        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        AuctionRequest updateAuctionRequest = new AuctionRequest(
                LocalDateTime.of(2025, 3, 28, 10, 0, 0, 0),
                LocalDateTime.of(2025, 4, 11, 10, 0, 0, 0)
        );

        PostRequest updatePostRequest = new PostRequest(
                "updateTitle",
                "updateContent",
                2000,
                "updatePlace",
                true,
                true,
                updateAuctionRequest);

        // when
        postService.modifyPost(savedPost.get().getId(), updatePostRequest, files, member.getId());
        postService.changeToAuction(savedPost.get().getId(), updateAuctionRequest, member.getId());

        Optional<Post> updatedPost = postRepository.findById(savedPost.get().getId());

        // then
        assertThat(updatedPost.isPresent()).isTrue();
        assertThat(updatedPost.get().getAuctionDetails().getStartedAt()).isEqualTo(updateAuctionRequest.getStartedAt());
        assertThat(updatedPost.get().getAuctionDetails().getClosedAt()).isEqualTo(updateAuctionRequest.getClosedAt());
    }

    @Test
    @DisplayName("판매글을 경매글로 전환")
    void t10() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "testPlace",
                true,
                false,
                null);

        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        AuctionRequest auctionRequest = new AuctionRequest(
                LocalDateTime.of(2025, 3, 27, 10, 0, 0, 0),
                LocalDateTime.of(2025, 4, 10, 10, 0, 0, 0)
        );

        // when
        postService.changeToAuction(savedPost.get().getId(), auctionRequest, member.getId());

        Optional<Post> updatedPost = postRepository.findById(savedPost.get().getId());

        // then
        assertThat(updatedPost.isPresent()).isTrue();
        assertThat(updatedPost.get().getAuctionDetails().getStartedAt()).isEqualTo(auctionRequest.getStartedAt());
        assertThat(updatedPost.get().getAuctionDetails().getClosedAt()).isEqualTo(auctionRequest.getClosedAt());
    }

    @Test
    @DisplayName("게시글 삭제")
    void t11() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "testPlace",
                true,
                false,
                null);

        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        // when
        postService.deletePost(savedPost.get().getId(), member.getId());

        Optional<Post> deletedPost = postRepository.findById(savedPost.get().getId());

        // then
        assertThat(deletedPost.isPresent()).isFalse();
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 작성자가 아닌 경우")
    void t12() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "testPlace",
                true,
                false,
                null);

        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        // when
        // then
        assertThatThrownBy(() -> postService.deletePost(savedPost.get().getId(), 2L))
                .isInstanceOf(PostException.class)
                .hasMessageContaining(PostErrorCode.UNAUTHORIZED_ACCESS.getMessage());

    }

    @Test
    @DisplayName("게시글 목록 조회")
    void t13() throws Exception {
        // given
        for (int i = 1; i <= 30; i++) {
            PostRequest request = new PostRequest(
                    "제목 " + i,
                    "내용 " + i,
                    i * 1000,
                    "부산광역시 금정구 장전동",
                    true,
                    false,
                    null
            );
            postService.createPost(request, files, member.getId());
        }

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        // when
        Page<PostListDto> result = postService.getPosts(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(30);
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("제목 30");
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void t14() throws Exception {
        // given
        PostRequest postRequest = new PostRequest(
                "testTitle",
                "testContent",
                1000,
                "testPlace",
                true,
                false,
                null);

        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        // when
        PostDto postDto = postService.getPost(savedPost.get().getId(), member.getId());

        // then
        assertThat(postDto.getTitle()).isEqualTo(postRequest.getTitle());
        assertThat(postDto.getContent()).isEqualTo(postRequest.getContent());
        assertThat(postDto.getSaleStatus()).isEqualTo(postRequest.getSaleStatus());
        assertThat(postDto.getPrice()).isEqualTo(postRequest.getPrice());
        assertThat(postDto.getPlace()).isEqualTo(postRequest.getPlace());
    }

    @Test
    @DisplayName("경매글 상세 조회")
    void t15() throws Exception {
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

        postService.createPost(postRequest, files, member.getId());

        Optional<Post> savedPost = postRepository.findFirstByOrderByIdDesc();

        // when
        PostDto postDto = postService.getPost(savedPost.get().getId(), member.getId());

        // then
        assertThat(postDto.getTitle()).isEqualTo(postRequest.getTitle());
        assertThat(postDto.getContent()).isEqualTo(postRequest.getContent());
        assertThat(postDto.getSaleStatus()).isEqualTo(postRequest.getSaleStatus());
        assertThat(postDto.getPrice()).isEqualTo(postRequest.getPrice());
        assertThat(postDto.getPlace()).isEqualTo(postRequest.getPlace());
        assertThat(postDto.getAuctionStartedAt()).isEqualTo(auctionRequest.getStartedAt());
        assertThat(postDto.getAuctionClosedAt()).isEqualTo(auctionRequest.getClosedAt());
    }

    @Test
    @DisplayName("게시글 검색 테스트 - 단일 검색")
    void t16() throws Exception {
        // given
        for (int i = 1; i <= 30; i++) {
            PostRequest request = new PostRequest(
                    "제목 " + i,
                    "내용 " + i,
                    i * 1000,
                    "부산광역시 금정구 장전동",
                    true,
                    false,
                    null
            );
            postService.createPost(request, files, member.getId());
        }

        // 검색 조건 설정
        PostSearchRequest searchRequest = new PostSearchRequest(
                1000L, 5000L, true, "제목 1", "부산광역시 금정구 장전동"
        );

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        // when
        Page<PostListDto> result = postService.getPostsBySearch(pageable, searchRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).contains("제목 1");
    }

    @Test
    @DisplayName("게시글 검색 테스트 - 리스트 검색")
    void t17() throws Exception {
        // given
        for (int i = 1; i <= 30; i++) {
            PostRequest request = new PostRequest(
                    "제목 " + i,
                    "내용 " + i,
                    i * 1000,
                    "부산광역시 금정구 장전동",
                    true,
                    false,
                    null
            );
            postService.createPost(request, files, member.getId());
        }

        // 검색 조건 설정
        PostSearchRequest searchRequest = new PostSearchRequest(
                1000L, 5000L, true, "", ""
        );

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        // when
        Page<PostListDto> result = postService.getPostsBySearch(pageable, searchRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(5L);
        assertThat(result.getContent()).hasSize(5);
    }
}
