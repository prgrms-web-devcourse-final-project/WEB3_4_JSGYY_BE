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
        List<ImageFileDto> uploadedImages = postImageService.uploadImages(postId, images);

        Map<String, Object> response = Map.of(
                "message", postId + "번 게시글 이미지 업로드 완료",
                "uploadedImages", uploadedImages
        );

        return ResponseEntity.ok(response);
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
