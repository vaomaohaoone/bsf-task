package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.math.BigInteger
import java.util.*

@ApiModel("Information about account")
data class AccountDto(
    @ApiModelProperty("Id of account")
    var id: UUID? = null,
    @ApiModelProperty("Name of account")
    var accountName: String? = null,
    @ApiModelProperty("Balance of account (with kopecks)")
    var balanceSum: BigInteger? = null,
    @ApiModelProperty("User id, which linked to account")
    var userId: String? = null
)
