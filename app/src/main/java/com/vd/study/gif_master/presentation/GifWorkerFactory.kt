package com.vd.study.gif_master.presentation

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.vd.study.viewing.domain.usecases.UpdateGifInLocalDatabaseUseCase
import com.vd.study.viewing.presentation.worker.GifWorker
import javax.inject.Inject

class GifWorkerFactory @Inject constructor(
    private val updateGifInLocalDatabaseUseCase: UpdateGifInLocalDatabaseUseCase
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            GifWorker::class.java.name -> {
                GifWorker(appContext, workerParameters, updateGifInLocalDatabaseUseCase)
            }

            else -> null
        }
    }
}
