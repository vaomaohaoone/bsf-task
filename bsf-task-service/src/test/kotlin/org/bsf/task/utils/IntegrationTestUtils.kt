package org.bsf.task.utils

import org.bsf.task.entity.AccountEntity
import org.bsf.task.entity.TransactionEntity
import org.bsf.task.enums.TransactionType
import org.bsf.task.repository.AccountRepository
import org.bsf.task.repository.TransactionRepository
import org.bsf.task.utils.TestUtils.Companion.randomAccountName
import org.bsf.task.utils.TestUtils.Companion.randomUserId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.util.*

@Component
class IntegrationTestUtils {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    fun createAndSaveAccount(balance: BigInteger, userId: String? = null, deleted: Boolean = false) =
        accountRepository.save(
            AccountEntity(
                accountName = randomAccountName(),
                balanceSum = balance,
                userId = userId ?: randomUserId(),
                isDeleted = deleted
            )
        )

    @Transactional
    fun createAndSaveTransaction(
        receiverId: UUID,
        senderId: UUID,
        type: TransactionType,
        sum: BigInteger
    ): TransactionEntity {
        val receiver = accountRepository.findById(receiverId).get()
        val sender = accountRepository.findById(senderId).get()
        return transactionRepository.save(
            TransactionEntity(
                receiver = receiver,
                sender = sender,
                transactionType = type,
                sum = sum
            )
        )
    }


}