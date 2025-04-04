package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.service.PostImageService;
import com.ll.nbe344team7.global.imageFIle.ImageFileDto;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;


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
            @RequestPart(value = "deleteImageIds", required = false) List<Long> deleteImageIds,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        if (images == null) images = new MultipartFile[0];
        if (deleteImageIds == null) deleteImageIds = List.of();

        List<ImageFileDto> uploadedImages = postImageService.updateImages(postId, images, deleteImageIds, userDetails.getMemberId());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("message", postId + "번 게시글 이미지 업로드 및 삭제 완료");
        response.put("uploadedImages", uploadedImages);
        response.put("deleteImageIds", deleteImageIds);

        return ResponseEntity.ok(response);
    }
}
