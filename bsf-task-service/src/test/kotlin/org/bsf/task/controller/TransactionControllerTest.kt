package org.bsf.task.controller

import com.fasterxml.jackson.module.kotlin.readValue
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.bsf.task.AbstractIntegrationTest
import org.bsf.task.dto.TransactionDto
import org.bsf.task.enums.TransactionType
import org.bsf.task.utils.TestUtils.Companion.createTransactionCreateDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import java.math.BigInteger

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



}