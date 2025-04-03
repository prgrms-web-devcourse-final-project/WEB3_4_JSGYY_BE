package com.ll.nbe344team7.domain.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostImageController {

    @PostMapping("/{postId}/images")
    public ResponseEntity<?> uploadImages(
            @PathVariable Long postId,
            @RequestPart(value = "images") List<MultipartFile> images
    ) {
        return ResponseEntity.ok(Map.of("message", postId + "번 게시글 이미지 업로드 완료"));
    }

    @DeleteMapping("/{postId}/images/{imageId}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long postId,
            @PathVariable Long imageId,
            @RequestPart(value = "images") List<MultipartFile> images
    ) {
        return ResponseEntity.ok(Map.of("message", postId + "번 게시글 " + imageId + "번 이미지 삭제 완료"));
    }
}
