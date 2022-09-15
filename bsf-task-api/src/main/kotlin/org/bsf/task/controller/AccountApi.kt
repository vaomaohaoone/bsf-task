package org.bsf.task.controller

import org.bsf.task.dto.*
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

interface AccountApi {

    @PostMapping(BASE_URI)
    fun createAccount(@Valid @RequestBody dto: AccountCreateDto): AccountDto

    @PutMapping(BASE_URI)
    fun updateAccountName(@RequestBody dto: UpdateAccountNameDto): AccountDto

    @GetMapping("$BASE_URI/{accountId}")
    fun getAccountById(@PathVariable("accountId") accountId: UUID): AccountDto

    @DeleteMapping(BASE_URI)
    fun deleteAccountById(@RequestBody dto: DeleteAccountDto)

    @GetMapping(BASE_URI)
    fun listAccounts(): List<AccountDto>

    /**
     * I don't use RequestMapping annotation on class, because in new version of FeignClient this is not allowed
     * */
    companion object {
        const val BASE_URI = "/api/v1/account"
    }
}