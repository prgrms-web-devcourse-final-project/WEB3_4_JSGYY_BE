package com.ll.nbe344team7.domain.auction.controller;

import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import com.ll.nbe344team7.domain.auction.service.AuctionService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@RestController
@RequestMapping("/api/auction")
@Tag(name = "경매 API")
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
    @Operation(summary = "입찰 기능")
    @PostMapping("/{postId}/bid")
    public ResponseEntity<?> bidPrice(@RequestBody BidDTO bidDTO, @PathVariable Long postId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(this.auctionService.bidPrice(bidDTO, postId, userDetails.getMemberId()));
    }
}
