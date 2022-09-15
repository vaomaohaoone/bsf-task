package org.bsf.task.utils

import org.apache.commons.lang3.RandomStringUtils
import org.bsf.task.dto.AccountCreateDto
import org.bsf.task.dto.TransactionCreateDto
import org.bsf.task.enums.TransactionType
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class TestUtils {

    companion object {
        fun createAccountCreateDto(initialSum: BigInteger): AccountCreateDto {
            return AccountCreateDto(
                randomAccountName(),
                initialSum,
                randomUserId()
            )
        }

        fun createTransactionCreateDto(
            receiverId: UUID,
            senderId: UUID,
            transactionType: TransactionType,
            sum: BigInteger
        ): TransactionCreateDto {
            return TransactionCreateDto(
                receiverId, senderId, transactionType, sum
            )
        }

        fun randomAccountName(): String =
            RandomStringUtils.randomAlphabetic(20)

        fun randomUserId(): String =
            RandomStringUtils.randomAlphabetic(10)
    }
}