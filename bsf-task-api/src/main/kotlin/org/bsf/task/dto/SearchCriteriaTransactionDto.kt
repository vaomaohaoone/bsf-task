package org.bsf.task.dto

import org.bsf.task.enums.TransactionType
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

data class SearchCriteriaTransactionDto(
    val receiverId: UUID? = null,
    val senderId: UUID? = null,
    val transactionType: TransactionType? = null,
    val sum: RangeDto<BigInteger>? = null,
    val date: RangeDto<LocalDateTime>? = null,
    val page: PageDto = PageDto(),
    val sortBy: SortDto? = null
) {
    class RangeDto<T>(
        val from: T? = null,
        val to: T? = null
    ) {
        fun notEmptyRange() =
            if (from != null || to != null) this else null
    }

    class SortDto(
        val field: String,
        val order: String = "ASC"
    )

    class PageDto(
        val number: Int = 0,
        val size: Int = 10
    )
}
