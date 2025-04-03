package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.auction.entity.Auction;
import com.ll.nbe344team7.domain.auction.repository.AuctionRepository;
import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.post.dto.request.AuctionRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostRequest;
import com.ll.nbe344team7.domain.post.dto.request.PostSearchRequest;
import com.ll.nbe344team7.domain.post.dto.response.PostDto;
import com.ll.nbe344team7.domain.post.dto.response.PostListDto;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.entity.PostLike;
import com.ll.nbe344team7.domain.post.exception.PostErrorCode;
import com.ll.nbe344team7.domain.post.exception.PostException;
import com.ll.nbe344team7.domain.post.repository.PostLikeRepository;
import com.ll.nbe344team7.domain.post.repository.PostRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.ll.nbe344team7.global.imageFIle.entity.ImageFile;
import com.ll.nbe344team7.global.imageFIle.repository.ImageFileRepository;
import com.ll.nbe344team7.global.imageFIle.service.S3ImageService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final S3ImageService s3ImageService;
    private final ImageFileRepository imageFileRepository;

    public PostService(PostRepository postRepository,
                       AuctionRepository auctionRepository,
                       MemberRepository memberRepository,
                       PostLikeRepository postLikeRepository,
                       S3ImageService s3ImageService,
                       ImageFileRepository imageFileRepository
    ) {
        this.postRepository = postRepository;
        this.auctionRepository = auctionRepository;
        this.memberRepository = memberRepository;
        this.postLikeRepository = postLikeRepository;
        this.s3ImageService = s3ImageService;
        this.imageFileRepository = imageFileRepository;
    }

    /**
     *
     * PostRequest 입력값 검증
     *
     * @param request
     *
     * @author GAEUN220
     * @since 2025-03-26
     */
    private void validatePostRequest(PostRequest request) {
        if (request.getTitle().isBlank() || request.getTitle().length() > 50) {
            throw new PostException(PostErrorCode.INVALID_TITLE);
        }

        if (request.getContent().isBlank() || request.getContent().length() > 500) {
            throw new PostException(PostErrorCode.INVALID_CONTENT);
        }

        if (request.getPrice() < 0) {
            throw new PostException(PostErrorCode.INVALID_PRICE);
        }

        if (request.getPlace().isBlank()) {
            throw new PostException(PostErrorCode.INVALID_PLACE);
        }
    }

    /**
     *
     * AucftionRequest 입력값 검증
     *
     * @param auctionRequest
     *
     * @author GAEUN220
     * @since 2025-04-02
     */
    private void validateAuctionRequest(AuctionRequest auctionRequest) {
        if (auctionRequest.getStartedAt().isAfter(auctionRequest.getClosedAt())) {
            throw new PostException(PostErrorCode.INVALID_AUCTION_DATE);
        }
    }

    /**
     *
     * 이미지 수 검증 (최대 10개)
     *
     * @param images
     *
     * @author GAEUN220
     * @since 2025-04-02
     */
    private void validateImageRequest(MultipartFile[] images) {
        if (images.length > 10) {
            throw new PostException(PostErrorCode.INVALID_IMAGE_COUNT);
        }
    }


    /**
     *
     * 게시글 작성 - 이미지 파일 O
     *
     * @param request
     * @param images
     * @param memberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-01
     */
    @Transactional
    public Map<String, String> createPost(PostRequest request, MultipartFile[] images, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        validatePostRequest(request);
        validateImageRequest(images);

        Post post = new Post(
                member,
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getPlace(),
                request.getAuctionStatus()
        );

        final Post savedPost = postRepository.save(post);

        saveFiles(images, savedPost);

        // 경매 상태가 true, AuctionRequest가 null이 아닐 경우
        if (request.getAuctionStatus() && request.getAuctionRequest() != null) {
            AuctionRequest auctionRequest = request.getAuctionRequest();

            validateAuctionRequest(auctionRequest);

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
     * 게시글 작성 - 이미지 파일 X (테스트 코드용)
     *
     * @param request
     * @param memberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-01
     */
    @Transactional
    public Map<String, String> createPost(PostRequest request, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        validatePostRequest(request);

        Post post = new Post(
                member,
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getPlace(),
                request.getAuctionStatus()
        );

        postRepository.save(post);

        // 경매 상태가 true, AuctionRequest가 null이 아닐 경우
        if (request.getAuctionStatus() && request.getAuctionRequest() != null) {
            AuctionRequest auctionRequest = request.getAuctionRequest();

            validateAuctionRequest(auctionRequest);

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

        if (!post.getMember().getId().equals(memberId)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        postLikeRepository.deleteByPostId(postId);
        postRepository.delete(post);

        return Map.of("message", postId + "번 게시글이 삭제되었습니다.");
    }

    /**
     *
     * 게시글 수정 - 이미지 O
     *
     * @param postId
     * @param request
     * @param images
     * @param memberId
     *
     * @author GAEUN220
     * @since 2025-04-01
     */
    @Transactional
    public void modifyPost(Long postId, PostRequest request, MultipartFile[] images, Long memberId) {

        validatePostRequest(request);
        validateImageRequest(images);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(memberId)) {
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

        updateFile(images, post);
    }

    /**
     *
     * 게시글 수정 - 이미지 X (테스트 코드 용)
     *
     * @param postId
     * @param request
     * @param memberId
     *
     * @author GAEUN220
     * @since 2025-04-01
     */
    @Transactional
    public void modifyPost(Long postId, PostRequest request, Long memberId) {

        validatePostRequest(request);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(memberId)) {
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

        boolean isLiked = postLikeRepository.existsByPostIdAndMemberId(postId, memberId);

        return PostDto.Companion.from(post, memberId, isLiked);
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

        if (!post.getMember().getId().equals(memberId)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (post.getAuctionDetails() != null) {
            Auction existingAuction = post.getAuctionDetails();

            validateAuctionRequest(auctionRequest);

            existingAuction.updateAuction(auctionRequest.getStartedAt(), auctionRequest.getClosedAt());

            auctionRepository.save(existingAuction);
        } else if (post.getAuctionDetails() == null) {
            post.updateAuctionStatus(true);

            validateAuctionRequest(auctionRequest);

            Auction auction = post.createAuction(
                    auctionRequest.getStartedAt(),
                    auctionRequest.getClosedAt()
            );

            postRepository.save(post);
            auctionRepository.save(auction);
        }
    }

    /**
     *
     * 게시글 좋아요
     *
     * @param postId
     * @param memberId
     * @return
     *
     * @author GAEUN220
     * @since 2025-03-31
     */
    @Transactional
    public Map<String, String> likePost(Long postId, Long memberId) {
        Post post = postRepository.findByIdWithLock(postId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId)) {
            throw new PostException(PostErrorCode.ALREADY_LIKED);
        }

        PostLike postLike = new PostLike(member, post);
        postLikeRepository.save(postLike);

        post.like();
        postRepository.save(post);

        return Map.of("message", postId + "번 게시글 좋아요 성공");
    }

    /**
     *
     * 게시글 좋아요 취소
     *
     * @param postId
     * @param memberId
     * @return
     *
     * @since 2025-03-31
     */
    @Transactional
    public Map<String, String> unlikePost(Long postId, Long memberId) {
        Post post = postRepository.findByIdWithLock(postId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post).orElseThrow(() -> new PostException(PostErrorCode.POST_LIKE_NOT_FOUND));
        postLikeRepository.delete(postLike);

        post.unlike();
        postRepository.save(post);

        return Map.of("message", postId + "번 게시글 좋아요 취소 성공");
    }

    /**
     *
     * 파일 업로도 + POST 연결
     *
     * @param images
     * @param post
     *
     * @author GAEUN220
     * @since 2025-04-02
     */
    private void saveFiles(final MultipartFile[] images, final Post post) {
        if (images != null && images.length > 0) {
            List<ImageFile> imageFiles = Arrays.stream(images)
                    .map(image -> {
                        String imageUrl = s3ImageService.upload(image); // S3 업로드
                        ImageFile imageFile = new ImageFile(imageUrl, post);
                        return imageFile;
                    })
                    .collect(Collectors.toList());

            imageFileRepository.saveAll(imageFiles);
            post.getImages().addAll(imageFiles);
        }
    }

    /**
     *
     * 기존 이미지 삭제 후 새 이미지 업로드 + POST 연결
     *
     * @param images
     * @param post
     *
     * @author GAEUN220
     * @since 2025-04-02
     */
    private void updateFile(MultipartFile[] images, final Post post) {
        if (images != null && images.length > 0) {
            // 기존 이미지 삭제 (S3 + DB)
            List<ImageFile> existingImages = post.getImages();
            existingImages.forEach(image -> {
                s3ImageService.deleteImageFromS3(image.getUrl());
                imageFileRepository.delete(image);
            });
            post.getImages().clear();

            // 새 이미지 업로드
            List<ImageFile> newImages = Arrays.stream(images)
                    .map(image -> {
                        String imageUrl = s3ImageService.upload(image); // S3 업로드
                        ImageFile imageFile = new ImageFile(imageUrl, post);
                        imageFile.setPost(post); // 게시글과 연관 설정
                        return imageFile;
                    })
                    .collect(Collectors.toList());

            imageFileRepository.saveAll(newImages);
            post.getImages().addAll(newImages);
        }
    }
}
