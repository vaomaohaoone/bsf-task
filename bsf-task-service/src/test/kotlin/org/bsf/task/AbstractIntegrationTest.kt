package org.bsf.task

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import org.bsf.task.repository.AccountRepository
import org.bsf.task.repository.TransactionRepository
import org.bsf.task.utils.IntegrationTestUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var integrationTestUtils: IntegrationTestUtils

    @LocalServerPort
    var serverPort: Int = -1

    @BeforeEach
    fun setupRestAssured() {
        RestAssured.port = serverPort
    }

    @AfterEach
    fun clean() {
        transactionRepository.deleteAll()
        accountRepository.deleteAll()
    }
}