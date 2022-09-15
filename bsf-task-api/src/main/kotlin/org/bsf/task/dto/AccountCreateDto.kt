package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.math.BigInteger
import javax.validation.constraints.Min

@ApiModel("Creating account dto")
data class AccountCreateDto(
    @ApiModelProperty("Name of account", required = true)
    val accountName: String? = null,
    @ApiModelProperty("Initial balance of account (with kopecks)", required = true)
    @field:Min(value = 0)
    val initialSum: BigInteger? = null,
    @ApiModelProperty("User id, which linked to account", required = true)
    val userId: String? = null
)
