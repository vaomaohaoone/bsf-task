package org.bsf.task.controller

import com.fasterxml.jackson.module.kotlin.readValue
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.bsf.task.AbstractIntegrationTest
import org.bsf.task.dto.SearchCriteriaTransactionDto
import org.bsf.task.dto.TransactionDto
import org.bsf.task.dto.TransactionListDto
import org.bsf.task.entity.AccountEntity
import org.bsf.task.enums.TransactionType
import org.bsf.task.utils.TestUtils.Companion.createTransactionCreateDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

internal class TransactionControllerTest : AbstractIntegrationTest() {

    @Test
    fun `create transaction successfully`() {
        val receiver = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val sender = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val command =
            createTransactionCreateDto(receiver.id!!, sender.id!!, TransactionType.WITHDRAW, BigInteger.valueOf(7000))
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/transaction")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<TransactionDto>(response)
            assertEquals(BigInteger.valueOf(7000), actual.sum)
            assertEquals(TransactionType.WITHDRAW, actual.type)
            assertEquals(receiver.id, actual.receiverId)
            assertEquals(sender.id, actual.senderId)
            assertNotNull(actual.createdOn)
            assertNotNull(actual.id)
            assertEquals(BigInteger.valueOf(17000), accountRepository.findById(receiver.id!!).get().balanceSum)
            assertEquals(BigInteger.valueOf(13000), accountRepository.findById(sender.id!!).get().balanceSum)
        }
    }

    @Test
    fun `get transaction by id successfully`() {
        val receiver = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val sender = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val transaction = integrationTestUtils.createAndSaveTransaction(
            receiver.id!!, sender.id!!, TransactionType.PAY, BigInteger.valueOf(7000)
        )
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            get("/api/v1/transaction/${transaction.id}")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<TransactionDto>(response)
            assertEquals(transaction.sum, actual.sum)
            assertEquals(transaction.transactionType, actual.type)
            assertEquals(transaction.receiver!!.id, actual.receiverId)
            assertEquals(transaction.sender!!.id, actual.senderId)
            assertEquals(transaction.createdOn, actual.createdOn)
            assertEquals(transaction.id, actual.id)
        }
    }

    @Test
    fun `list transactions by receiverId sort by sum`() {
        val firstAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val secondAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val thirdAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(30000))
        val trans1 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = secondAccount.id!!, type = TransactionType.PAY,
            BigInteger.valueOf(3000)
        )
        val trans2 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(5000)
        )
        val trans3 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(4000)
        )
        val trans4 = integrationTestUtils.createAndSaveTransaction(
            receiverId = secondAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(2000)
        )
        val command = SearchCriteriaTransactionDto(
            receiverId = firstAccount.id,
            sortBy = SearchCriteriaTransactionDto.SortDto(
                "sum", "DESC"
            )
        )
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/transaction/list")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<TransactionListDto>(response)
            assertEquals(3, actual.rows.size)
            assertEquals(trans2.id, actual.rows[0].id)
            assertEquals(trans3.id, actual.rows[1].id)
            assertEquals(trans1.id, actual.rows[2].id)
            assertEquals(3, actual.totalRows)
            assertEquals(1, actual.pagesCount)
        }
    }

    @Test
    fun `list transactions by senderId sort by date`() {
        val firstAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val secondAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val thirdAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(30000))
        val trans1 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = secondAccount.id!!, type = TransactionType.PAY,
            BigInteger.valueOf(3000)
        )
        val trans2 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(5000)
        )
        val trans3 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(4000)
        )
        val trans4 = integrationTestUtils.createAndSaveTransaction(
            receiverId = secondAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(2000)
        )
        val command = SearchCriteriaTransactionDto(
            senderId = thirdAccount.id,
            sortBy = SearchCriteriaTransactionDto.SortDto(
                "date", "ASC"
            )
        )
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/transaction/list")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<TransactionListDto>(response)
            assertEquals(3, actual.rows.size)
            assertEquals(trans2.id, actual.rows[0].id)
            assertEquals(trans3.id, actual.rows[1].id)
            assertEquals(trans4.id, actual.rows[2].id)
            assertEquals(3, actual.totalRows)
            assertEquals(1, actual.pagesCount)
        }
    }

    @Test
    fun `list transactions by transactionType with pagination sort by sum`() {
        val firstAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val secondAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val thirdAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(30000))
        val trans1 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = secondAccount.id!!, type = TransactionType.PAY,
            BigInteger.valueOf(3000)
        )
        val trans2 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(5000)
        )
        val trans3 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(4000)
        )
        val trans4 = integrationTestUtils.createAndSaveTransaction(
            receiverId = secondAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(2000)
        )
        val command = SearchCriteriaTransactionDto(
            transactionType = TransactionType.WITHDRAW,
            page = SearchCriteriaTransactionDto.PageDto(
                0, 2
            ),
            sortBy = SearchCriteriaTransactionDto.SortDto(
                "sum", "DESC"
            )
        )
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/transaction/list")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<TransactionListDto>(response)
            assertEquals(2, actual.rows.size)
            assertEquals(trans2.id, actual.rows[0].id)
            assertEquals(trans3.id, actual.rows[1].id)
            assertEquals(3, actual.totalRows)
            assertEquals(2, actual.pagesCount)
        }
    }

    @Test
    fun `list transactions by sum range sort by sum`() {
        val firstAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val secondAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val thirdAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(30000))
        val trans1 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = secondAccount.id!!, type = TransactionType.PAY,
            BigInteger.valueOf(3000)
        )
        val trans2 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(5000)
        )
        val trans3 = integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(4000)
        )
        val trans4 = integrationTestUtils.createAndSaveTransaction(
            receiverId = secondAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(2000)
        )
        val command = SearchCriteriaTransactionDto(
            sum = SearchCriteriaTransactionDto.RangeDto(BigInteger.valueOf(2000), BigInteger.valueOf(3000)),
            sortBy = SearchCriteriaTransactionDto.SortDto(
                "sum", "DESC"
            )
        )
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/transaction/list")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<TransactionListDto>(response)
            assertEquals(2, actual.rows.size)
            assertEquals(trans1.id, actual.rows[0].id)
            assertEquals(trans4.id, actual.rows[1].id)
            assertEquals(2, actual.totalRows)
            assertEquals(1, actual.pagesCount)
        }
    }

    @Test
    fun `list transactions by date range`() {
        val firstAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val secondAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val thirdAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(30000))
        integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = secondAccount.id!!, type = TransactionType.PAY,
            BigInteger.valueOf(3000)
        )
        integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(5000)
        )
        integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(4000)
        )
        integrationTestUtils.createAndSaveTransaction(
            receiverId = secondAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(2000)
        )
        val command = SearchCriteriaTransactionDto(
            date = SearchCriteriaTransactionDto.RangeDto(
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1)
            )
        )
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/transaction/list")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<TransactionListDto>(response)
            assertEquals(4, actual.rows.size)
            assertEquals(4, actual.totalRows)
            assertEquals(1, actual.pagesCount)
        }
    }

    @Test
    fun `sort by unsupported field should return 400 status`() {
        val firstAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(10000))
        val secondAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        val thirdAccount = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(30000))
        integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = secondAccount.id!!, type = TransactionType.PAY,
            BigInteger.valueOf(3000)
        )
        integrationTestUtils.createAndSaveTransaction(
            receiverId = firstAccount.id!!, senderId = thirdAccount.id!!, type = TransactionType.WITHDRAW,
            BigInteger.valueOf(5000)
        )
        val command = SearchCriteriaTransactionDto(
            sortBy = SearchCriteriaTransactionDto.SortDto("receiverId", "ASC")
        )
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/transaction/list")
        } Then {
            statusCode(400)
        }
    }

    @Test
    fun `try parallel`() {
        val receiver = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(0))
        val sender = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(20000))
        launchParallel(receiver, sender, 100)
        val allTrans = transactionRepository.findAll()
        assertEquals(1, allTrans.size)
        assertEquals(BigInteger.valueOf(20000), allTrans[0].sum)
        assertEquals(BigInteger.ZERO, accountRepository.findById(sender.id!!).get().balanceSum)
        assertEquals(BigInteger.valueOf(20000), accountRepository.findById(receiver.id!!).get().balanceSum)
    }

    fun launchParallel(receiver: AccountEntity, sender: AccountEntity, count: Int) {
        val countOfThreads = 100
        val service = Executors.newFixedThreadPool(10);
        val latch = CountDownLatch(countOfThreads);
        var i = 0
        while (i < countOfThreads) {
            service.submit {
                val command =
                    createTransactionCreateDto(
                        receiver.id!!,
                        sender.id!!,
                        TransactionType.WITHDRAW,
                        BigInteger.valueOf(20000)
                    )
                Given {
                    contentType(MediaType.APPLICATION_JSON_VALUE)
                    body(command)
                } When {
                    post("/api/v1/transaction")
                } Then {

                }
                latch.countDown()
            }
            i++
        }
        latch.await()
    }

}