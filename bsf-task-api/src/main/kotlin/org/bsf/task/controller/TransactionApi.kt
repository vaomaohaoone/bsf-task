package org.bsf.task.controller

import org.bsf.task.dto.SearchCriteriaTransactionDto
import org.bsf.task.dto.TransactionCreateDto
import org.bsf.task.dto.TransactionDto
import org.bsf.task.dto.TransactionListDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

interface TransactionApi {

    @PostMapping("$BASE_URI/list")
    fun listTransactions(@RequestBody searchCriteria: SearchCriteriaTransactionDto): TransactionListDto

    @PostMapping(BASE_URI)
    fun createTransaction(@RequestBody dto: TransactionCreateDto): TransactionDto

    @GetMapping("$BASE_URI/{transactionId}")
    fun getTransactionById(@PathVariable("transactionId") transactionId: UUID): TransactionDto

    /**
     * I don't use RequestMapping annotation on class, because in new version of FeignClient this is not allowed
     * */
    companion object {
        const val BASE_URI = "/api/v1/transaction"
    }
}