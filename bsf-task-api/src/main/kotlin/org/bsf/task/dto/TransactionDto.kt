package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.bsf.task.enums.TransactionType
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

@ApiModel("Transaction create dto")
data class TransactionDto(
    @ApiModelProperty("Transaction id")
    val id: UUID? = null,
    @ApiModelProperty("Creation date")
    val createdOn: LocalDateTime? = null,
    @ApiModelProperty("Receiver account id")
    val receiverId: UUID? = null,
    @ApiModelProperty("Sender account id")
    val senderId: UUID? = null,
    @ApiModelProperty("Transaction type")
    val type: TransactionType? = null,
    @ApiModelProperty("Transaction sum")
    val sum: BigInteger? = null
)