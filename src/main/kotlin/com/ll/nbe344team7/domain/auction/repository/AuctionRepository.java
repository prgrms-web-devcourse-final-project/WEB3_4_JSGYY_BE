package com.ll.nbe344team7.domain.auction.repository;

import com.ll.nbe344team7.domain.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author shjung
 * @since 25. 3. 25.
 */
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    public Optional<Auction> findByPostId(Long postId);
}
