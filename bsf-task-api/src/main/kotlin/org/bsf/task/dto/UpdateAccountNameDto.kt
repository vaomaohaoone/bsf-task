package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

@ApiModel("Update account name command")
data class UpdateAccountNameDto(
    @ApiModelProperty("Id of account")
    val id: UUID,
    @ApiModelProperty("Name of account")
    val accountName: String,
)