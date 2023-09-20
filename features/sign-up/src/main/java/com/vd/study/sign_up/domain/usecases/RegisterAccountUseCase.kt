package com.vd.study.sign_up.domain.usecases

import com.vd.study.core.IODispatcher
import javax.inject.Inject
import com.vd.study.core.Result
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.domain.repositories.RegistrationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RegisterAccountUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: RegistrationRepository
) {

    suspend operator fun invoke(account: AccountEntity): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext repository.registerAccount(account)
    }
}