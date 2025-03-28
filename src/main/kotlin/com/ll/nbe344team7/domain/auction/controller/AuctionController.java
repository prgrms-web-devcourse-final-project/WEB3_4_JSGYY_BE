package com.ll.nbe344team7.domain.auction.controller;

import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import com.ll.nbe344team7.domain.auction.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/{postId}/bid")
    public ResponseEntity<?> bidPrice(@RequestBody BidDTO bidDTO, @PathVariable Long postId) {
        if(bidDTO.getMemberId() == null || bidDTO.getMemberId() == 10000) {
            return ResponseEntity.status(404).body(Map.of("message", "멤버가 조회되지 않습니다."));
        }
        else if(postId == null || postId == 10000) {
            return ResponseEntity.status(404).body(Map.of("message", "게시글이 조회되지 않습니다."));
        }
        else if(bidDTO.getPrice() == null || bidDTO.getPrice() > 50000) {
            return ResponseEntity.status(404).body(Map.of("message", "보유금보다 많은 금액을 입찰할 수는 없습니다."));
        }

        return ResponseEntity.ok(this.auctionService.bidPrice(bidDTO, postId));
    }
}
