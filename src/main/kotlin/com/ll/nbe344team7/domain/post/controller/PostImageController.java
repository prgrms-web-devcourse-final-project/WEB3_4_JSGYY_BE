package com.ll.nbe344team7.domain.post.controller;

import com.ll.nbe344team7.domain.post.dto.response.UpdateImageResult;
import com.ll.nbe344team7.domain.post.service.PostImageService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;


@RestController
@RequestMapping("/api/posts")
@Tag(name = "게시글 이미지 API")
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
    @Operation(
            summary = "게시글 이미지 업로드 및 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "이미지만 업로드",
                                            value = """
                                            {
                                              "message": "1번 게시글 이미지 업로드 및 삭제 완료",
                                              "uploadedImages": [
                                                {
                                                  "id": 1,
                                                  "url": "image.jpg"
                                                }
                                              ],
                                              "deletedImageIds": []
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "이미지 삭제만",
                                            value = """
                                            {
                                                "message": "1번 게시글 이미지 업로드 및 삭제 완료",
                                                "uploadedImages": [],
                                                "deletedImageIds": [
                                                    9
                                                ]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "이미지 업로드 + 삭제",
                                            value = """
                                            {
                                              "message": "1번 게시글 이미지 업로드 및 삭제 완료",
                                              "uploadedImages": [
                                                {
                                                  "id": 4,
                                                  "url": "image.jpg"
                                                }
                                              ],
                                              "deletedImageIds": [1,2]
                                            }
                                            """
                                    )
                            }
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 게시글입니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "맴버 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "멤버가 조회되지 않습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "403", description = "게시글 권한 오류", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "게시글 권한이 없습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "최소 이미지 오류", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "게시글에는 최소 1장의 이미지가 있어야 합니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "최대 이미지 오류", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "이미지는 최대 10개까지 업로드 가능합니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "이미지 조회 오류", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "존재하지 않는 이미지입니다."
                                            }
                                            """
                            )
                    )),
            }
    )
    @PostMapping(path = "/{postId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImages(
            @PathVariable Long postId,
            @RequestBody MultipartFile[] images,
//            @RequestPart(value = "deleteImageIds", required = false) List<Long> deleteImageIds,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        if (images == null) images = new MultipartFile[0];
//        if (deleteImageIds == null) deleteImageIds = List.of();

        UpdateImageResult result = postImageService.updateImages(postId, images, userDetails.getMemberId());

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("message", postId + "번 게시글 이미지 업로드 완료");
        response.put("uploadedImages", result.getUploadedImages());
//        response.put("deletedImageIds", result.getDeletedImageIds());

        return ResponseEntity.ok(response);
    }

    @PostMapping(path="/{postId}/images/delete")
    public ResponseEntity<?> deleteImages(
            @PathVariable Long postId,
            @RequestBody List<Long> deletedImageIds,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        if (deletedImageIds == null) deletedImageIds = List.of();

        List<Long> deleteIds = postImageService.deleteImages(deletedImageIds);

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("message", postId + "번 게시글 이미지 삭제 완료");
        response.put("deletedImageIds", deleteIds);

        return ResponseEntity.ok(response);
    }
}
