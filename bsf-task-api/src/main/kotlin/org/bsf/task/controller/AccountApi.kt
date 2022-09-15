package org.bsf.task.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.bsf.task.dto.AccountCreateDto
import org.bsf.task.dto.AccountDto
import org.bsf.task.dto.DeleteAccountDto
import org.bsf.task.dto.UpdateAccountNameDto
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@Api(value = "account", description = "Operations with accounts")
interface AccountApi {

    @ApiOperation(value = "Create new account")
    @PostMapping(BASE_URI)
    fun createAccount(@Valid @RequestBody dto: AccountCreateDto): AccountDto

    @ApiOperation(value = "Update account name")
    @PutMapping(BASE_URI)
    fun updateAccountName(@RequestBody dto: UpdateAccountNameDto): AccountDto

    @ApiOperation(value = "Fetch account by account id")
    @GetMapping("$BASE_URI/{accountId}")
    fun getAccountById(@PathVariable("accountId") accountId: UUID): AccountDto

    @ApiOperation(value = "Delete account by id with money transfer on target account")
    @DeleteMapping(BASE_URI)
    fun deleteAccountById(@RequestBody dto: DeleteAccountDto)

    @ApiOperation(value = "List all non-deleted accounts")
    @GetMapping(BASE_URI)
    fun listAccounts(): List<AccountDto>

    /**
     * I don't use RequestMapping annotation on class, because in new version of FeignClient this is not allowed
     * */
    companion object {
        const val BASE_URI = "/api/v1/account"
    }
}