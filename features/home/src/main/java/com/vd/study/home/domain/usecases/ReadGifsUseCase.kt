package com.vd.study.home.domain.usecases

import com.vd.study.core.IODispatcher
import com.vd.study.core.Result
import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.home.domain.repositories.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadGifsUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: HomeRepository
) {

    suspend operator fun invoke(): Result<GifEntity> = withContext(ioDispatcher) {
        return@withContext repository.readGifs()
    }
}