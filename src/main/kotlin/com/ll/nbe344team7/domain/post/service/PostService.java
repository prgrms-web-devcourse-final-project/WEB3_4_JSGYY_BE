package com.ll.nbe344team7.domain.post.service;

import com.ll.nbe344team7.domain.post.dto.AuctionRequest;
import com.ll.nbe344team7.domain.post.dto.PostRequest;
import org.springframework.stereotype.Service;

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
                "currentPage", 1,
                "totalPages", 1,
                "totalItems", 2,
                "pageSize", 10,
                "posts", List.of(
                        Map.of(
                                "id", 1,
                                "title", "제목1",
                                "content", "내용1",
                                "place", "부산광역시 금정구 장전동",
                                "price", 10000,
                                "status", true,
                                "auctionStatus", false,
                                "likes", 3,
                                "reports", 0
                        ),
                        Map.of(
                                "id", 2,
                                "title", "제목2",
                                "content", "내용2",
                                "place", "서울특별시 강남구 역삼동",
                                "price", 15000,
                                "status", true,
                                "auctionStatus", false,
                                "likes", 5,
                                "reports", 1
                        )
                )
        );
    }

    public Map<String, Object> getPost(Long postId) {
        return Map.of(
                "id", 1,
                "title", "제목",
                "content", "내용",
                "place", "부산광역시 금정구 장전동",
                "price", 10000,
                "status", true,
                "actionStatus", false,
                "likes", 3,
                "reports", 0
        );
    }

    public Map<String, Object> changeToAuction(Long postId, AuctionRequest request) {
        return Map.of("message", "경매 전환이 완료되었습니다.");
    }

}
