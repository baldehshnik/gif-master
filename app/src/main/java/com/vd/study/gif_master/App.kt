package com.vd.study.gif_master

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.vd.study.gif_master.presentation.GifWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var gifWorkerFactory: GifWorkerFactory

    override fun onCreate() {
        super.onCreate()
        val configuration = Configuration.Builder()
            .setWorkerFactory(gifWorkerFactory)
            .build()

        WorkManager.initialize(applicationContext, configuration)
    }
}