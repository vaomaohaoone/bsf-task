package org.bsf.task.dto

import io.swagger.annotations.ApiModel

@ApiModel("Error response dto")
data class ErrorResponseDto(
    val errorMessage: String
)