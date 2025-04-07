package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.member.entity.Member;
import com.ll.nbe344team7.domain.member.repository.MemberRepository;
import com.ll.nbe344team7.domain.post.dto.response.UpdateImageResult;
import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.exception.PostErrorCode;
import com.ll.nbe344team7.domain.post.exception.PostException;
import com.ll.nbe344team7.domain.post.repository.PostRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import com.ll.nbe344team7.global.imageFIle.ImageFileDto;
import com.ll.nbe344team7.global.imageFIle.entity.ImageFile;
import com.ll.nbe344team7.global.imageFIle.repository.ImageFileRepository;
import com.ll.nbe344team7.global.imageFIle.service.S3ImageService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostImageService {
    private final S3ImageService s3ImageService;
    private final ImageFileRepository imageFileRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private static final Logger log = LoggerFactory.getLogger(PostImageService.class);

    public PostImageService(S3ImageService s3ImageService,
                            ImageFileRepository imageFileRepository,
                            PostRepository postRepository,
                            MemberRepository memberRepository)
    {
        this.s3ImageService = s3ImageService;
        this.imageFileRepository = imageFileRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    /**
     *
     * 게시글 사진 업로드&삭제
     *
     * @param postId
     * @param images
     * @param deleteImageIds
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-04
     */
    @Transactional
    public UpdateImageResult updateImages(Long postId, MultipartFile[] images, List<Long> deleteImageIds, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER));

        if (!member.getId().equals(post.getMember().getId())) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_ACCESS);
        }

        List<ImageFile> uploadedImages = uploadImages(post, images);
        List<Long> successfullyDeleted = deleteImages(deleteImageIds);

        List<ImageFile> finalImages = imageFileRepository.findByPostId(postId);

        if (finalImages.isEmpty()) {
            throw new PostException(PostErrorCode.IMAGE_REQUIRED);
        }

        if(finalImages.size() > 10) {
            throw new PostException(PostErrorCode.INVALID_IMAGE_COUNT);
        }

        List<ImageFileDto> uploadedDtos = uploadedImages.stream()
                .map(ImageFileDto.Companion::from)
                .collect(Collectors.toList());

        return new UpdateImageResult(uploadedDtos, successfullyDeleted);
    }

    /**
     *
     * 사진 업로드
     *
     * @param post
     * @param images
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-04
     */
    public List<ImageFile> uploadImages(Post post, MultipartFile[] images) {
        List<ImageFile> imageFiles = new ArrayList<>();

        if (images != null && images.length > 0) {
            imageFiles = Arrays.stream(images)
                    .map(image -> {
                        String imageUrl = s3ImageService.upload(image); // S3 업로드
                        return new ImageFile(imageUrl, post);
                    })
                    .collect(Collectors.toList());

            imageFileRepository.saveAll(imageFiles);
            post.getImages().addAll(imageFiles);
        }

        return imageFiles;
    }

    /**
     *
     * 사진 삭제 (S3, DB)
     *
     * @param deleteImageIds
     *
     * @author GAEUN220
     * @since 2025-04-04
     */
    public List<Long> deleteImages(List<Long> deleteImageIds) {
        List<Long> successfullyDeleted = new ArrayList<>();

        for (Long imageId : deleteImageIds) {
            try {
                ImageFile imageFile = imageFileRepository.findById(imageId)
                        .orElseThrow(() -> new PostException(PostErrorCode.IMAGE_NOT_FOUND));

                s3ImageService.deleteImageFromS3(imageFile.getUrl());
                imageFileRepository.delete(imageFile);
                successfullyDeleted.add(imageId); // 삭제 성공한 것만 추가
            } catch (PostException e) {
                log.debug("이미지 삭제 중 오류 발생 (imageId: {}): {}", imageId, e.getMessage());
            }
        }

        return successfullyDeleted;
    }
}
