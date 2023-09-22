package com.vd.study.settings.domain.usecases

import com.vd.study.core.IODispatcher
import com.vd.study.core.Result
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.domain.repositories.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadAccountUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(email: String): Result<AccountEntity> = withContext(ioDispatcher) {
        return@withContext repository.readAccount(email)
    }
}