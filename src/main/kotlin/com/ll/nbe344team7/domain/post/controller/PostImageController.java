package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.service.PostImageService;
import com.ll.nbe344team7.global.imageFIle.ImageFileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/posts")
public class PostImageController {
    private final PostImageService postImageService;

    public PostImageController(PostImageService postImageService) {
        this.postImageService = postImageService;
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
    @PostMapping("/{postId}/images")
    public ResponseEntity<?> updateImages(
            @PathVariable Long postId,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "deleteImageIds", required = false) List<Long> deleteImageIds
    ) {
        List<ImageFileDto> uploadedImages = postImageService.updateImages(postId, images, deleteImageIds);

        Map<String, Object> response = Map.of(
                "message", postId + "번 게시글 이미지 업로드 및 삭제 완료",
                "uploadedImages", uploadedImages,
                "deleteImageIds", deleteImageIds
        );

        return ResponseEntity.ok(response);
    }
}
