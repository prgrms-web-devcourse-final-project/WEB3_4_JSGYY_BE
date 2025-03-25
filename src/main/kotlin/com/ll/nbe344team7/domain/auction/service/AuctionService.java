package com.ll.nbe344team7.domain.auction.service;

import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class AuctionService {

    public Map<Object, Object> bidPrice(BidDTO dto, Long postId){

        return Map.of("message", postId + "번 게시글 책 물품에 15000원 입찰이 완료되었습니다.");
    }
}
