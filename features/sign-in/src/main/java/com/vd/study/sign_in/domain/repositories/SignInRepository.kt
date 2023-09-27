package com.vd.study.sign_in.domain.repositories

import com.vd.study.sign_in.domain.entities.AccountEntity
import com.vd.study.core.container.Result

interface SignInRepository {

    suspend fun readAccounts(): Result<List<AccountEntity>>

    suspend fun isAccountExistsAndCorrect(account: AccountEntity): Result<Boolean>

}