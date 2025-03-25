package com.ll.nbe344team7.domain.auction.service;

import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import com.ll.nbe344team7.domain.auction.entity.Auction;
import com.ll.nbe344team7.domain.auction.repository.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public Map<Object, Object> bidPrice(BidDTO dto, Long postId){
        Optional<Auction> op = auctionRepository.findByPostId(postId);

        Auction auction = op.orElse(null);
        if(auction == null){
            throw new NullPointerException();
        }

        if(auction.getMaxPrice() < dto.getPrice()){
            auction.setMaxPrice(dto.getPrice());
            auction.setMemberId(dto.getMemberId());
            auction = auctionRepository.save(auction);
        } else if(auction.getMaxPrice() > dto.getPrice()){
            throw new RuntimeException();
        }

        return Map.of("message", postId + "번 게시글 물품에 " + auction.getMaxPrice() + "원 입찰이 완료되었습니다.");
    }
}
