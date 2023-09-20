package com.vd.study.sign_up.domain.repositories

import com.vd.study.core.Result
import com.vd.study.sign_up.domain.entities.AccountEntity

interface RegistrationRepository {

    suspend fun registerAccount(account: AccountEntity): Result<Boolean>

}