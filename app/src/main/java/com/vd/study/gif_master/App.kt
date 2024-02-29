package com.vd.study.gif_master

import android.app.Application
import android.os.Build
//import coil.Coil
//import coil.ImageLoader
//import coil.decode.GifDecoder
//import coil.decode.ImageDecoderDecoder
//import coil.memory.MemoryCache
//import coil.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    // check before deleting
    override fun onCreate() {
        super.onCreate()
//        Coil.setImageLoader(
//            ImageLoader.Builder(applicationContext)
//                .memoryCachePolicy(CachePolicy.ENABLED)
//                .memoryCache {
//                    MemoryCache.Builder(applicationContext)
//                        .maxSizePercent(0.25)
//                        .build()
//                }
//                .components {
//                    if (Build.VERSION.SDK_INT >= 28) {
//                        add(ImageDecoderDecoder.Factory())
//                    } else {
//                        add(GifDecoder.Factory())
//                    }
//                }
//                .build()
//        )
    }
}