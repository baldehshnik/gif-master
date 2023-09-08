package com.vd.study.data.remote.di

import com.vd.study.data.remote.entities.GiphyCore
import com.vd.study.data.remote.sources.GifsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourcesModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(GiphyCore.ConnectTimeout, TimeUnit.SECONDS)
            .readTimeout(GiphyCore.ReadTimeout, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGifsApiService(client: OkHttpClient): GifsApiService {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(GiphyCore.BASE_GIFS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofitBuilder.create(GifsApiService::class.java)
    }
}