package org.bsf.task.dto

import io.swagger.annotations.ApiModel
import java.util.*

@ApiModel("Delete account command")
data class DeleteAccountDto(
    val deleteAccountId: UUID,
    val targetAccountId: UUID? = null
)
