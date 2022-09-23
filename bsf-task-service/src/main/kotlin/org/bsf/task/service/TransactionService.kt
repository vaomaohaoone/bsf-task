package org.bsf.task.service

import org.bsf.task.dto.SearchCriteriaTransactionDto
import org.bsf.task.dto.TransactionDto
import org.bsf.task.dto.TransactionListDto
import org.bsf.task.entity.TransactionEntity
import org.bsf.task.enums.TransactionType
import org.bsf.task.exception.BsfBusinessException
import org.bsf.task.repository.TransactionPagingRepository
import org.bsf.task.repository.TransactionRepository
import org.bsf.task.utils.mapEntityToDto
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val transactionPagingRepository: TransactionPagingRepository,
    @Lazy
    private val accountService: AccountService
) {
    @Transactional(readOnly = true)
    fun listTransactions(searchCriteria: SearchCriteriaTransactionDto): TransactionListDto {
        val transactionPage = transactionPagingRepository.findBySearchCriteria(searchCriteria)
        val rows = transactionPage.content.map { row ->
            row.mapEntityToDto()
        }
        return TransactionListDto(rows, transactionPage.totalPages, transactionPage.totalElements)
    }

    @Transactional(readOnly = true)
    fun getTransactionById(id: UUID): TransactionDto =
        transactionRepository.findById(id).orElseThrow { EntityNotFoundException("Transaction with id=$id not found") }
            .mapEntityToDto()

    @Transactional
    fun createTransaction(senderId: UUID, receiverId: UUID, sum: BigInteger, type: TransactionType): TransactionDto {
        val sender = accountService.getAccountByIdOrThrow(senderId)
        val receiver = accountService.getAccountByIdOrThrow(receiverId)
        if (sender.balanceSum >= sum) {
            sender.balanceSum = sender.balanceSum.minus(sum)
            receiver.balanceSum = receiver.balanceSum.plus(sum)
        } else
            throw BsfBusinessException("Sender balance is lower than requested")
        return transactionRepository.save(
            TransactionEntity(
                receiver = receiver,
                sender = sender,
                transactionType = type,
                sum = sum
            )
        ).mapEntityToDto()
    }

}