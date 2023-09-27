package com.vd.study.sign_in.domain.usecases

import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.core.container.Result
import com.vd.study.sign_in.domain.entities.AccountEntity
import com.vd.study.sign_in.domain.repositories.SignInRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IsAccountExistsAndCorrectUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: SignInRepository
) {

    suspend operator fun invoke(account: AccountEntity): Result<Boolean> = withContext(ioDispatcher) {
            return@withContext repository.isAccountExistsAndCorrect(account)
    }
}