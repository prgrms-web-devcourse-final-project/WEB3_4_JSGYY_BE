package com.ll.nbe344team7.domain.pay.repository;

import com.ll.nbe344team7.domain.pay.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shjung
 * @since 25. 3. 25.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Exchange, Long> {
    public List<Exchange> findByMyId(Long memberId);

    public List<Exchange> findByMyIdAndExchangeType(Long memberId, Integer exchangeType);

    public long countByImpUidContainsIgnoreCase(String impUid);

    Exchange findByMyIdAndPostId(Long myId, Long postId);
}
