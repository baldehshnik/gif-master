package com.vd.study.home.domain.usecases

import androidx.paging.PagingData
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.home.domain.repositories.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PagingReadGifsUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: HomeRepository
) {

    suspend operator fun invoke(): Flow<PagingData<GifEntity>> = withContext(ioDispatcher) {
        return@withContext repository.pagingReadGifs()
    }
}