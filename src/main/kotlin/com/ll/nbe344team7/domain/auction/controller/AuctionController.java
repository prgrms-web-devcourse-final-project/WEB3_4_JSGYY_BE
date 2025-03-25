package com.ll.nbe344team7.domain.auction.controller;

import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import com.ll.nbe344team7.domain.auction.exception.AuctionException;
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

    /**
     *
     * 입찰 기능
     *
     * @param bidDTO
     * @param postId
     * @return
     *
     * @author shjung
     * @since 25. 3. 25.
     */
    @PostMapping("/{postId}/bid")
    public ResponseEntity<?> bidPrice(@RequestBody BidDTO bidDTO, @PathVariable Long postId) {
        try{
            return ResponseEntity.ok(this.auctionService.bidPrice(bidDTO, postId));
        } catch (AuctionException e) {
            return ResponseEntity.status(e.getStatus()).body(Map.of("message", e.getMessage()));
        }
    }
}
