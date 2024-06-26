package com.vd.study.data

import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.core.container.Result
import com.vd.study.data.local.accounts.entities.CheckAccountDataEntity

interface AccountsDataRepository {

    suspend fun saveNewAccount(account: AccountDataEntity): Result<Boolean>

    suspend fun readAccounts(): Result<List<AccountDataEntity>>

    suspend fun removeAccount(accountId: Int): Result<Boolean>

    suspend fun updateAccount(account: AccountDataEntity): Result<Boolean>

    suspend fun readAccount(email: String): Result<AccountDataEntity>

    suspend fun readAccountById(id: Int): Result<AccountDataEntity>

    suspend fun checkAccountExistence(account: CheckAccountDataEntity): Result<Int>

}
