package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Transaction list dto")
data class TransactionListDto(
    @ApiModelProperty("Transactions rows")
    val rows: List<TransactionDto> = listOf(),
    @ApiModelProperty("Page amount")
    val pagesCount: Int,
    @ApiModelProperty("Rows count")
    val totalRows: Long
)
