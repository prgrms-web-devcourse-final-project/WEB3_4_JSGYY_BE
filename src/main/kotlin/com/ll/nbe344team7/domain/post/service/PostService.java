package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.post.dto.PostRequest;
import org.springframework.stereotype.Service;

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

    public Map<String, Object> getPost(Long postId) {
        // 예제 데이터 (postId가 1인 경우만 존재)
        if (postId != 1) {
            return null; // 존재하지 않는 게시글
        }

        return Map.of(
                "id", 1,
                "title", "제목",
                "content", "내용",
    }
}
