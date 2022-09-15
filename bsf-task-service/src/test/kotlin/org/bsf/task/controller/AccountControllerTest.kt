package org.bsf.task.controller

import com.fasterxml.jackson.module.kotlin.readValue
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.bsf.task.AbstractIntegrationTest
import org.bsf.task.dto.AccountDto
import org.bsf.task.dto.DeleteAccountDto
import org.bsf.task.dto.UpdateAccountNameDto
import org.bsf.task.utils.TestUtils.Companion.createAccountCreateDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import java.math.BigInteger
import java.util.*

internal class AccountControllerTest : AbstractIntegrationTest() {

    @Test
    fun `create account successfully`() {
        val command = createAccountCreateDto(BigInteger.valueOf(1000000))
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/account")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<AccountDto>(response)
            assertEquals(command.accountName, actual.accountName)
            assertEquals(command.initialSum, actual.balanceSum)
            assertEquals(command.userId, actual.userId)
            assertNotNull(actual.id)
        }
    }

    @Test
    fun `create account with negative balance should return 400 status`() {
        val command = createAccountCreateDto(BigInteger.valueOf(-1))
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            post("/api/v1/account")
        } Then {
            statusCode(400)
        }
    }

    @Test
    fun `update user account name`() {
        val account = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(100000))
        val command = UpdateAccountNameDto(account.id!!, "updated-name")
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(command)
        } When {
            put("/api/v1/account")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<AccountDto>(response)
            assertEquals(command.accountName, actual.accountName)
            assertEquals(account.balanceSum, actual.balanceSum)
            assertEquals(account.userId, actual.userId)
            assertEquals(account.id, actual.id)
        }
    }

    @Test
    fun `get account info by id when exists`() {
        val account = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(100000))
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            get("/api/v1/account/${account.id}")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<AccountDto>(response)
            assertEquals(account.accountName, actual.accountName)
            assertEquals(account.balanceSum, actual.balanceSum)
            assertEquals(account.userId, actual.userId)
            assertEquals(account.id, actual.id)
        }
    }

    @Test
    fun `get deleted account returns 204 status`() {
        val deletedAccount = integrationTestUtils.createAndSaveAccount(BigInteger.ZERO, "user1", true)
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            get("/api/v1/account/${deletedAccount.id}")
        } Then {
            statusCode(204)
        }
    }

    @Test
    fun `get account info by id returns 404 when does not exists`() {
        val id = UUID.randomUUID()
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            get("/api/v1/account/$id")
        } Then {
            statusCode(404)
        }
    }

    @Test
    fun `list all accounts`() {
        integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(100000))
        integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(50000))
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        } When {
            get("/api/v1/account")
        } Then {
            statusCode(200)
            val response = contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response()
                .asString()
            val actual = objectMapper.readValue<List<AccountDto>>(response)
            assertEquals(2, actual.size)
        }
    }

    @Test
    fun `delete account successfully with new transaction`() {
        val delete = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(100000), "user1")
        val target = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(50000), "user1")
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(DeleteAccountDto(deleteAccountId = delete.id!!, targetAccountId = target.id!!))
        } When {
            delete("/api/v1/account")
        } Then {
            statusCode(200)
            assertEquals(1, transactionRepository.findAll().size)
            assertEquals(BigInteger.valueOf(100000), transactionRepository.findAll()[0].sum)
            val deleted = accountRepository.findById(delete.id!!).get()
            val targetUpdated = accountRepository.findById(target.id!!).get()
            assertEquals(true, deleted.isDeleted)
            assertEquals(BigInteger.ZERO, deleted.balanceSum)
            assertEquals(BigInteger.valueOf(150000), targetUpdated.balanceSum)
        }
    }

    @Test
    fun `delete account successfully with zero balance`() {
        val delete = integrationTestUtils.createAndSaveAccount(BigInteger.ZERO, "user1")
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(DeleteAccountDto(deleteAccountId = delete.id!!))
        } When {
            delete("/api/v1/account")
        } Then {
            statusCode(200)
            assertEquals(0, transactionRepository.findAll().size)
            val deleted = accountRepository.findById(delete.id!!).get()
            assertEquals(true, deleted.isDeleted)
            assertEquals(BigInteger.ZERO, deleted.balanceSum)
        }
    }

    @Test
    fun `delete account returns 400 status when users not equals`() {
        val delete = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(100000), "user1")
        val target = integrationTestUtils.createAndSaveAccount(BigInteger.valueOf(50000), "user2")
        Given {
            contentType(MediaType.APPLICATION_JSON_VALUE)
            body(DeleteAccountDto(deleteAccountId = delete.id!!, targetAccountId = target.id!!))
        } When {
            delete("/api/v1/account")
        } Then {
            statusCode(400)
        }
    }

}