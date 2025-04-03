package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.service.PostImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * @param postId
     * @param images
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-03
     */
    @PostMapping("/{postId}/images")
    public ResponseEntity<?> uploadImages(
            @PathVariable Long postId,
            @RequestPart(value = "images") MultipartFile[] images
    ) {
        postImageService.uploadImages(postId, images);

        return ResponseEntity.ok(Map.of("message", postId + "번 게시글 이미지 업로드 완료"));
    }

    /**
     *
     * @param imageId
     * @return
     *
     * @author GAEUN220
     * @since 2025-04-03
     */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long imageId
    ) {
        postImageService.deleteImage(imageId);

        return ResponseEntity.ok(Map.of("message",imageId + "번 이미지 삭제 완료"));
    }
}
