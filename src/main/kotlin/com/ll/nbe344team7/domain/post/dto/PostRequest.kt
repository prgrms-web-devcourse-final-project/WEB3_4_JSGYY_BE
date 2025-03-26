
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class PostRequest (
    @field:NotBlank(message = "제목을 입력해주세요.")
    val title: String,
    @field:NotBlank(message = "내용을 입력해주세요.")
    val content: String,
    @field:Min(1, message = "가격을 1원 이상 입력해주세요.")
    val price: Long,
    @field:NotBlank(message = "장소를 입력해주세요.")
    val place: String,
    val auctionStatus: Boolean,
    val auctionRequest: AuctionRequest?
)