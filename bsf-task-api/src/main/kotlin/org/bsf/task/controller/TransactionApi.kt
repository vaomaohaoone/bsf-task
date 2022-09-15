package org.bsf.task.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.bsf.task.dto.SearchCriteriaTransactionDto
import org.bsf.task.dto.TransactionCreateDto
import org.bsf.task.dto.TransactionDto
import org.bsf.task.dto.TransactionListDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Api(value = "transaction", description = "Operations with transactions")
interface TransactionApi {

    @ApiOperation(value = "List all transactions with pagination and filtering")
    @PostMapping("$BASE_URI/list")
    fun listTransactions(@RequestBody searchCriteria: SearchCriteriaTransactionDto): TransactionListDto

    @ApiOperation(value = "Create new transaction")
    @PostMapping(BASE_URI)
    fun createTransaction(@RequestBody dto: TransactionCreateDto): TransactionDto

    @ApiOperation(value = "Fetch transaction by transaction id")
    @GetMapping("$BASE_URI/{transactionId}")
    fun getTransactionById(@PathVariable("transactionId") transactionId: UUID): TransactionDto

    /**
     * I don't use RequestMapping annotation on class, because in new version of FeignClient this is not allowed
     * */
    companion object {
        const val BASE_URI = "/api/v1/transaction"
    }
}