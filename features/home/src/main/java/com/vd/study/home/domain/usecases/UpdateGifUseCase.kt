package com.vd.study.home.domain.usecases

import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.domain.repositories.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateGifUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: HomeRepository
) {

    suspend operator fun invoke(gif: FullGifEntity): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext repository.updateGif(gif)
    }
}