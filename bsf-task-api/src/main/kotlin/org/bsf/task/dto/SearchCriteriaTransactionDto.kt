package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.bsf.task.enums.TransactionType
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

@ApiModel("Search criteria dto for list transactions")
data class SearchCriteriaTransactionDto(
    @ApiModelProperty("Receiver id")
    val receiverId: UUID? = null,
    @ApiModelProperty("Sender id")
    val senderId: UUID? = null,
    @ApiModelProperty("Transaction type")
    val transactionType: TransactionType? = null,
    @ApiModelProperty("Sum range")
    val sum: RangeDto<BigInteger>? = null,
    @ApiModelProperty("Transactions date range")
    val date: RangeDto<LocalDateTime>? = null,
    @ApiModelProperty("Page number and size")
    val page: PageDto = PageDto(),
    @ApiModelProperty("Sort field")
    val sortBy: SortDto? = null
) {
    class RangeDto<T>(
        val from: T? = null,
        val to: T? = null
    ) {
        fun notEmptyRange() =
            if (from != null || to != null) this else null
    }

    @ApiModel("Sort dto")
    class SortDto(
        @ApiModelProperty("Sort field", allowableValues = "sum, date, type")
        val field: String,
        @ApiModelProperty("Order", allowableValues = "ASC, DESC")
        val order: String = "ASC"
    )

    class PageDto(
        val number: Int = 0,
        val size: Int = 10
    )
}
