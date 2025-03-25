package com.ll.nbe344team7.domain.auction.service;

import com.ll.nbe344team7.domain.account.entity.Account;
import com.ll.nbe344team7.domain.account.repository.AccountRepository;
import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import com.ll.nbe344team7.domain.auction.entity.Auction;
import com.ll.nbe344team7.domain.auction.exception.AuctionError;
import com.ll.nbe344team7.domain.auction.exception.AuctionException;
import com.ll.nbe344team7.domain.auction.repository.AuctionRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AccountRepository accountRepository;

    public AuctionService(AuctionRepository auctionRepository, AccountRepository accountRepository) {
        this.auctionRepository = auctionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     *
     * 입찰 기능
     *
     * @param dto
     * @param postId
     * @return
     *
     * @author shjung
     * @since 25. 3. 25.
     */
    public Map<Object, Object> bidPrice(BidDTO dto, Long postId){
        // 1. 게시글이 존재하는지 요청
        Auction auction = this.auctionRepository.findByPostId(postId);;
        if(auction == null){
            throw new GlobalException(GlobalExceptionCode.NOT_FOUND_POST);
        }

        // 2. 멤버가 존재 하는지 확인
        Account account = accountRepository.findByMemberId(dto.getMemberId());
        if(account == null){
            throw new GlobalException(GlobalExceptionCode.NOT_FOUND_MEMBER);
        }

        // 3. 보유금이 입찰한 가격보다 높은지 확인
        if(account.getMoney() >= dto.getPrice()){
            if(auction.getMaxPrice() < dto.getPrice()){
                auction.setMaxPrice(dto.getPrice());
                auction.setMemberId(dto.getMemberId());
                auction = auctionRepository.save(auction);
            }
        } else if(account.getMoney() < dto.getPrice()){
            throw new AuctionException(AuctionError.NOT_OVER_ACCOUNT);
        }

        return Map.of("message", postId + "번 게시글 물품에 " + auction.getMaxPrice() + "원 입찰이 완료되었습니다.");
    }
}
