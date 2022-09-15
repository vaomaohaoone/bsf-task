package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.bsf.task.enums.TransactionType
import java.math.BigInteger
import java.util.*
import javax.validation.constraints.Min

@ApiModel("Transaction create dto")
data class TransactionCreateDto(
    @ApiModelProperty("Receiver id", required = true)
    val receiverId: UUID,
    @ApiModelProperty("Sender id", required = true)
    val senderId: UUID,
    @ApiModelProperty("Transaction type", required = true, allowableValues = "PAY, WITHDRAW, CONTRIBUTION")
    val type: TransactionType,
    @ApiModelProperty("Transaction sum (with kopecks)", required = true)
    @field:Min(value = 0)
    val sum: BigInteger
)
