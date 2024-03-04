package com.vd.study.viewing.domain.usecases

import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.viewing.domain.repositories.ViewingRepository
import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateGifInLocalDatabaseUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: ViewingRepository
) {

    suspend operator fun invoke(gif: GifEntity): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext repository.updateGif(gif)
    }

}