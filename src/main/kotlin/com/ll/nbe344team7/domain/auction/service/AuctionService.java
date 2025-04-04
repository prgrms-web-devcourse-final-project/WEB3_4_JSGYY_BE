package com.ll.nbe344team7.domain.auction.service;

import com.ll.nbe344team7.domain.account.entity.Account;
import com.ll.nbe344team7.domain.account.repository.AccountRepository;
import com.ll.nbe344team7.domain.auction.dto.BidDTO;
import com.ll.nbe344team7.domain.auction.entity.Auction;
import com.ll.nbe344team7.domain.auction.entity.AuctionSchedule;
import com.ll.nbe344team7.domain.auction.exception.AuctionError;
import com.ll.nbe344team7.domain.auction.exception.AuctionException;
import com.ll.nbe344team7.domain.auction.repository.AuctionRepository;
import com.ll.nbe344team7.domain.auction.repository.AuctionScheduleRepository;
import com.ll.nbe344team7.global.exception.GlobalException;
import com.ll.nbe344team7.global.exception.GlobalExceptionCode;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author shjung
 * @since 25. 3. 24.
 */
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AccountRepository accountRepository;
    private final AuctionScheduleRepository auctionScheduleRepository;

    public AuctionService(AuctionRepository auctionRepository, AccountRepository accountRepository,
                          AuctionScheduleRepository auctionScheduleRepository) {
        this.auctionRepository = auctionRepository;
        this.accountRepository = accountRepository;
        this.auctionScheduleRepository = auctionScheduleRepository;
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
                // 4. 그 전에 입찰한 유저가 존재할 경우
                Account beforeAccount = accountRepository.findByMemberId(auction.getWinnerId());
                if(beforeAccount != null){
                    // 4-1. 보유금 수정
                    beforeAccount.setMoney(beforeAccount.getMoney() + auction.getMaxPrice());
                    this.accountRepository.save(beforeAccount);
                }
                // 5. 경매 최대값, 보유금 수정 및 저장
                auction.setMaxPrice(dto.getPrice());
                auction.setWinnerId(dto.getMemberId());
                account.setMoney(account.getMoney() - dto.getPrice());
                this.auctionRepository.save(auction);
                this.accountRepository.save(account);
            }
        } else if(account.getMoney() < dto.getPrice()){
            throw new AuctionException(AuctionError.NOT_OVER_ACCOUNT);
        }

        return Map.of("message", postId + "번 게시글 물품에 " + auction.getMaxPrice() + "원 입찰이 완료되었습니다.");
    }

    /**
     *
     * 1분마다 스케쥴링으로 경매 종료 시간 확인
     *
     * @author shjung
     * @since 25. 4. 4.
     */
    @Scheduled(fixedRate = 60000)
    public void winToBid(){
        checkAuctionSchedule();
    }

    /**
     *
     * 프로그램 시작 시에 종료 시간 있는 지 확인
     *
     * @author shjung
     * @since 25. 4. 4.
     */
    @PostConstruct
    public void onStart(){
        checkAuctionSchedule();
    }

    /**
     *
     * 경매 종료 시간 확인
     * - 경매가 종료되서 상태가 바뀐게 없는 경우에만 변경
     *
     * @author shjung
     * @since 25. 4. 4.
     */
    public void checkAuctionSchedule(){
        // 1. 1분 마다 현재 시간보다 전이고 변경되기 전인 경매가 있는 경우를 확인
        // - 따로 엔티티를 두어 확인
        List<AuctionSchedule> auctionSchedules = auctionScheduleRepository.findByExecutedIsFalseAndClosedTimeBefore(LocalDateTime.now());
        // 2. 있는 경우 경매 종료로 상태 변경
        if(!auctionSchedules.isEmpty()){
            for(AuctionSchedule auctionSchedule : auctionSchedules){
                // 3. 현재 경매 종료한 상태로 변경
                auctionSchedule.setExecuted(true);
                // 4. 경매를 조회 후에 변경
                Auction auction = this.auctionRepository.findById(auctionSchedule.getAuctionId()).orElse(null);
                // 5. 만약 null 일 경우 넘어가기
                if(auction == null) continue;
                auction.setStatus(1);
                // 6. 변경된 정보를 DB에 저장
                this.auctionRepository.save(auction);
                this.auctionScheduleRepository.save(auctionSchedule);
            }
        }
    }
}
