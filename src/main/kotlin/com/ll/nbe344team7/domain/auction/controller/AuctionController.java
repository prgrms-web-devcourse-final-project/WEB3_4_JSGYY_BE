package com.ll.nbe344team7.domain.auction.controller;

import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import com.ll.nbe344team7.domain.auction.service.AuctionService;
import com.ll.nbe344team7.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "입찰 기능",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "입찰 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BidDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "입찰 성공", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "성공 응답 예시",
                                    value = """
                                            {
                                                "message" : "1번 게시글 물품에 20000원 입찰이 완료되었습니다."
                                            }
                                            """
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "게시글 조회 실패", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "게시글이 조회되지 않습니다."
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
                    @ApiResponse(responseCode = "404", description = "입찰금 초과 오류", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message" : "보유금보다 높은 입찰가를 입찰할 수는 없습니다."
                                            }
                                            """
                            )
                    ))

            }
    )
    @PostMapping("/{postId}/bid")
    public ResponseEntity<?> bidPrice(@RequestBody BidDTO bidDTO, @PathVariable Long postId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(this.auctionService.bidPrice(bidDTO, postId, userDetails.getMemberId()));
    }
}
