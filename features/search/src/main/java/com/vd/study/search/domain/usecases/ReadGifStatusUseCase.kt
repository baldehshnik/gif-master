package com.vd.study.search.domain.usecases

import com.vd.study.core.container.Result
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.search.domain.entities.GifStatusEntity
import com.vd.study.search.domain.entities.NetworkGifEntity
import com.vd.study.search.domain.repositories.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadGifStatusUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(gif: NetworkGifEntity): Result<GifStatusEntity> = withContext(ioDispatcher) {
        return@withContext repository.readGifStatus(gif)
    }
}
