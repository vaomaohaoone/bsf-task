package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

@ApiModel("Delete account command")
data class DeleteAccountDto(
    @ApiModelProperty("Deleted account id", required = true)
    val deleteAccountId: UUID,
    @ApiModelProperty("Balance target account")
    val targetAccountId: UUID? = null
)
