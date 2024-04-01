package com.vd.study.sign_in.domain.usecases

import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.sign_in.domain.repositories.SignInRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import com.vd.study.core.container.Result
import com.vd.study.sign_in.domain.entities.AccountEntity
import kotlinx.coroutines.withContext

class ReadAccountsUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: SignInRepository
) {

    suspend operator fun invoke(): Result<List<AccountEntity>> = withContext(ioDispatcher) {
        return@withContext repository.readAccounts()
    }
}
