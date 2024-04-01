package com.vd.study.viewing.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vd.study.viewing.domain.usecases.UpdateGifInLocalDatabaseUseCase
import com.vd.study.viewing.presentation.utils.getGifEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class GifWorker @AssistedInject constructor(
    @Assisted("context") appContext: Context,
    @Assisted("params") params: WorkerParameters,
    private val updateGifInLocalDatabaseUseCase: UpdateGifInLocalDatabaseUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val gif = inputData.getGifEntity()
        updateGifInLocalDatabaseUseCase(gif)
        return Result.success()
    }
}
