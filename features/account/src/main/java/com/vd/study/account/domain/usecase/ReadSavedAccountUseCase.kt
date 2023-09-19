package com.vd.study.account.domain.usecase

import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.domain.repositories.AccountRepository
import com.vd.study.core.IODispatcher
import com.vd.study.core.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadSavedAccountUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: AccountRepository
) {

    suspend operator fun invoke(email: String): Result<AccountEntity> = withContext(ioDispatcher) {
        return@withContext repository.readAccount(email)
    }
}