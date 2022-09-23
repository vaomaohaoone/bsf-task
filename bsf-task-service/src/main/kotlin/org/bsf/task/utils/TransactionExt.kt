package org.bsf.task.utils

import org.bsf.task.dto.TransactionDto
import org.bsf.task.entity.TransactionEntity


fun TransactionEntity.mapEntityToDto(): TransactionDto =
    TransactionDto(
        this.id, this.createdOn, this.receiver?.id, this.sender?.id, this.transactionType, this.sum
    )