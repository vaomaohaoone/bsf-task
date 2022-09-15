package org.bsf.task.controller

import org.bsf.task.dto.SearchCriteriaTransactionDto
import org.bsf.task.dto.TransactionCreateDto
import org.bsf.task.dto.TransactionDto
import org.bsf.task.dto.TransactionListDto
import org.bsf.task.service.TransactionService
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TransactionController(
    private val transactionService: TransactionService
) : TransactionApi {

    override fun listTransactions(searchCriteria: SearchCriteriaTransactionDto): TransactionListDto =
        transactionService.listTransactions(searchCriteria)

    override fun createTransaction(dto: TransactionCreateDto): TransactionDto =
        transactionService.createTransactionAndGetResponse(dto)

    override fun getTransactionById(transactionId: UUID): TransactionDto =
        transactionService.getTransactionById(transactionId)
}