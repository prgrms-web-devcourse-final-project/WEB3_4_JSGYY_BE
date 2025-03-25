package com.ll.nbe344team7.domain.auction.enums

/**
 *
 * 경매 상태 enum
 *
 * @author shjung
 * @since 25. 3. 25.
 */
enum class AuctionStatus (val code: Int){
    READY(0), PROGRESS(1), COMPLETED(2);

    fun getValue(): Int {return code}
}