package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.post.entity.Post;
import com.ll.nbe344team7.domain.post.exception.PostErrorCode;
import com.ll.nbe344team7.domain.post.exception.PostException;
import com.ll.nbe344team7.domain.post.repository.PostRepository;
import com.ll.nbe344team7.global.imageFIle.ImageFileDto;
import com.ll.nbe344team7.global.imageFIle.entity.ImageFile;
import com.ll.nbe344team7.global.imageFIle.repository.ImageFileRepository;
import com.ll.nbe344team7.global.imageFIle.service.S3ImageService;
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

    public PostImageService(S3ImageService s3ImageService,
                            ImageFileRepository imageFileRepository,
                            PostRepository postRepository)
    {
        this.s3ImageService = s3ImageService;
        this.imageFileRepository = imageFileRepository;
        this.postRepository = postRepository;
    }

    public List<ImageFileDto> uploadImages(Long postId, MultipartFile[] images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        List<ImageFile> imageFiles = new ArrayList<>();

        if (images != null && images.length > 0) {
            imageFiles = Arrays.stream(images)
                    .map(image -> {
                        String imageUrl = s3ImageService.upload(image); // S3 업로드
                        ImageFile imageFile = new ImageFile(imageUrl, post);
                        return imageFile;
                    })
                    .collect(Collectors.toList());

            imageFileRepository.saveAll(imageFiles);
            post.getImages().addAll(imageFiles);
        }

        // **ImageFile 엔티티 리스트를 ImageFileDto 리스트로 변환하여 반환**
        return imageFiles.stream()
                .map(ImageFileDto.Companion::from)
                .collect(Collectors.toList());
    }


    public void deleteImage(Long imageId) {
        ImageFile imageFile = imageFileRepository.findById(imageId)
                .orElseThrow(() -> new PostException(PostErrorCode.IMAGE_NOT_FOUND));

        s3ImageService.deleteImageFromS3(imageFile.getUrl());
        imageFileRepository.delete(imageFile);
    }
}
