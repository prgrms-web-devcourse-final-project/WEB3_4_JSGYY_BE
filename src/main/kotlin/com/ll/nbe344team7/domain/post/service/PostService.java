package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.post.dto.PostRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Map<String, Object> getPosts() {
        return Map.of(
                "content", List.of(
                        Map.of(
                                "id", 1,
                                "title", "아디다스 바지",
                                "category", "의류",
                                "place", "서울특별시_관악구_신림동",
                                "price", 50000,
                                "saleStatus", true,
                                "auctionStatus", false,
                                "createdAt", "2025-03-27T10:00:00"
                        ),
                        Map.of(
                                "id", 2,
                                "title", "바지",
                                "category", "의류",
                                "place", "서울특별시_관악구_신림동",
                                "price", 30000,
                                "saleStatus", true,
                                "auctionStatus", false,
                                "createdAt", "2025-03-26T15:00:00"
                        )
                ),
                "page", Map.of(
                        "size", 10,
                        "totalElements", 50,
                        "totalPages", 5,
                        "number", 0,
                        "hasNext", true,
                        "hasPrevious", false,
                        "isFirst", true,
                        "isLast", false,
                        "sort", Map.of(
                                "sorted", true,
                                "unsorted", false,
                                "empty", false
                        )
                )
        );
    }

    public Map<String, Object> getPost(Long postId) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", 1);
        response.put("authorId", 101);
        response.put("title", "아디다스 바지");
        response.put("content", "아디다스 바지입니다.");
        response.put("place", "서울특별시_관악구_신림동");
        response.put("price", 50000);
        response.put("saleStatus", true);
        response.put("auctionStatus", false);
        response.put("likes", 100);
        response.put("reports", 2);
        response.put("createdAt", "2025-03-27T10:00:00");
        response.put("modifiedAt", "2025-03-27T11:00:00");
        response.put("isAuthor", true);
        return response;
    }
}
