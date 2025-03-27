package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.auction.entity.Auction;
import com.ll.nbe344team7.domain.auction.repository.AuctionRepository;
import com.ll.nbe344team7.domain.post.dto.request.AuctionRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostSearchRequest;
import com.ll.nbe344team7.domain.post.dto.response.PostDto;
import com.ll.nbe344team7.domain.post.dto.response.PostListDto;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.exception.PostErrorCode;
import com.ll.nbe344team7.domain.post.exception.PostException;

import com.ll.nbe344team7.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuctionRepository auctionRepository;

    public PostService(PostRepository postRepository, AuctionRepository auctionRepository) {
        this.postRepository = postRepository;
        this.auctionRepository = auctionRepository;
    }

    /**
     *
     * 입력값 검증
     *
     * @param request
     *
     * @author GAEUN220
     * @since 2025-03-26
     */
    private void validatePostRequest(PostRequest request) {
        if (request.getTitle().trim().isEmpty() || request.getTitle().length() > 50) {
            throw new PostException(PostErrorCode.INVALID_TITLE);
        }

        if (request.getContent().trim().isEmpty() || request.getContent().length() > 500) {
            throw new PostException(PostErrorCode.INVALID_CONTENT);
        }

        if (request.getPrice() < 0) {
            throw new PostException(PostErrorCode.INVALID_PRICE);
        }

        if (request.getPlace().trim().isEmpty()) {
            throw new PostException(PostErrorCode.INVALID_PLACE);
        }
    }


    /**
     *
     * 게시글 작성
     *
     * @param request
     * @param memberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-25
     */
    @Transactional
    public Map<String, String> createPost(PostRequest request, Long memberId) {

        validatePostRequest(request);

        Post post = new Post(
                memberId,
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getPlace(),
                request.getAuctionStatus()
        );

        post = postRepository.save(post);

        // 경매 상태가 true, AuctionRequest가 null이 아닐 경우
        if (request.getAuctionStatus() && request.getAuctionRequest() != null) {
            AuctionRequest auctionRequest = request.getAuctionRequest();

            Auction auction = post.createAuction(
                    auctionRequest.getStartedAt(),
                    auctionRequest.getClosedAt()
            );

            auctionRepository.save(auction);
        }

        // 반환 메시지
        return Map.of("message", post.getId() + "번 게시글이 작성되었습니다.");
    }

    /**
     *
     * 게시글 삭제
     *
     * @param postId
     * @param memberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-25
     */
    public Map<String, String> deletePost(Long postId, Long memberId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (post.getMemberId() != memberId) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        postRepository.delete(post);

        return Map.of("message", postId + "번 게시글이 삭제되었습니다.");
    }

    /**
     *
     * 게시글 수정
     *
     * @param postId
     * @param request
     * @param memberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-25
     */
    @Transactional
    public void modifyPost(Long postId, PostRequest request, Long memberId) {

        validatePostRequest(request);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (post.getMemberId() != memberId) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getPlace(),
                request.getSaleStatus(),
                request.getAuctionStatus()
        );

        postRepository.save(post);
    }

    /**
     *
     * 게시글 목록 조회
     *
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-26
     */
    public Page<PostListDto> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> PostListDto.Companion.from(post));
    }

    /**
     *
     * 게시글 검색
     *
     * @param pageable
     * @param searchRequest
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-26
     */
    public Page<PostListDto> getPostsBySearch(Pageable pageable, PostSearchRequest searchRequest) {

        if (searchRequest.getMinPrice() == null && searchRequest.getMaxPrice() == null
                && searchRequest.getSaleStatus() == null && searchRequest.getKeyword() == null) {
            return postRepository.findAll(pageable).map(post -> PostListDto.Companion.from(post));
        }

        return postRepository.findBySearchCriteria(
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice(),
                searchRequest.getSaleStatus(),
                searchRequest.getKeyword(),
                searchRequest.getPlace(),
                pageable
        ).map(post -> PostListDto.Companion.from(post));
    }

    /**
     *
     * 게시글 상세 조회
     *
     * @param postId
     * @param memberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-26
     */
    public PostDto getPost(Long postId, Long memberId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        return PostDto.Companion.from(post, memberId);
    }


    /**
     *
     * 게시글 경매 전환
     *
     * @param postId
     * @param auctionRequest
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-26
     */
    public void changeToAuction(Long postId, AuctionRequest auctionRequest, Long memberId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (post.getMemberId() != memberId) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (post.getAuctionDetails() != null) {
            Auction existingAuction = post.getAuctionDetails();

            existingAuction.updateAuction(auctionRequest.getStartedAt(), auctionRequest.getClosedAt());

            auctionRepository.save(existingAuction);
        } else if (post.getAuctionDetails() == null) {
            post.updateAuctionStatus(true);

            Auction auction = post.createAuction(
                    auctionRequest.getStartedAt(),
                    auctionRequest.getClosedAt()
            );

            auctionRepository.save(auction);
            postRepository.save(post);
        }
    }
}
