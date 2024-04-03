package com.vd.study.settings.domain.usecases

import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.settings.domain.repositories.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadSavedGifsCountUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(): Result<Int> = withContext(ioDispatcher) {
        return@withContext repository.readSavedGifsCount()
    }
}
