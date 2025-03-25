package com.ll.nbe344team7.domain.auction.enums

/**
 *
 * 경매 상태 enum
 *
 * @author shjung
 * @since 25. 3. 25.
 */
enum class AuctionStatus (val code: String){
    READY("ready"), PROGRESS("progress"), COMPLETED("completed")
}