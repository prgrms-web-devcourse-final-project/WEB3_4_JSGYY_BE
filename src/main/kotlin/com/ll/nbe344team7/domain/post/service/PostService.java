package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.post.dto.PostDto;
import com.ll.nbe344team7.domain.post.dto.PostImageDto;
import com.ll.nbe344team7.domain.post.dto.PostListDto;
import com.ll.nbe344team7.domain.post.dto.PostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    public Map<String, Object> createPost(PostRequest request) {
        return Map.of("message", "1번 게시글이 작성되었습니다.");
    }

    public Map<String, Object> deletePost(Long postId) {
        return Map.of("message", postId + "번 게시글이 삭제되었습니다.");
    }

    public Map<String, Object> modifyPost(Long postId, PostRequest request) {
        return Map.of("message", postId + "번 게시글이 수정되었습니다.");
    }

    public Page<PostListDto> getPosts() {
        List<PostListDto> posts = List.of(
                new PostListDto(1L, "아디다스 바지", 50000L, true, "/images/adidas_pants.jpg", LocalDateTime.of(2025, 3, 27, 10, 0)),
                new PostListDto(2L, "바지", 30000L, true, "/images/pants.jpg", LocalDateTime.of(2025, 3, 26, 15, 0))
        );

        return new PageImpl<>(posts, PageRequest.of(0, 10), 50);
    }

    public PostDto getPost(Long postId) {
        return new PostDto(
                postId, 1L, "아디다스 바지", "아디다스 바지입니다.", "의류",
                "서울특별시_관악구_신림동", 50000L, true, true,
                LocalDateTime.parse("2025-03-27T10:00:00"),
                LocalDateTime.parse("2025-03-29T11:00:00"),
                100, 2,
                LocalDateTime.parse("2025-03-27T10:00:00"),
                LocalDateTime.parse("2025-03-27T11:00:00"),
                true,
                List.of(new PostImageDto(1L, "adidas_pants.jpg", "/images/adidas_pants.jpg", 204800L))
        );
    }
}
