package org.bsf.task.dto

import org.bsf.task.enums.TransactionType
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

data class TransactionDto(
    val id: UUID? = null,
    val createdOn: LocalDateTime? = null,
    val receiverId: UUID? = null,
    val senderId: UUID? = null,
    val type: TransactionType? = null,
    val sum: BigInteger? = null
)