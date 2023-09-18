package com.vd.study.data

import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.core.Result

interface AccountsDataRepository {

    suspend fun saveNewAccount(account: AccountDataEntity): Result<Boolean>

    suspend fun readAccounts(): Result<List<AccountDataEntity>>

    suspend fun removeAccount(account: AccountDataEntity): Result<Boolean>

    suspend fun updateAccount(account: AccountDataEntity): Result<Boolean>

    suspend fun readAccount(email: String): Result<AccountDataEntity>

}