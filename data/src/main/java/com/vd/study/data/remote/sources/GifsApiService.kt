package com.vd.study.data.remote.sources

import com.vd.study.data.BuildConfig
import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.remote.entities.GiphyCore
import retrofit2.Call
import retrofit2.http.GET

interface GifsApiService {

    @GET("${GiphyCore.TrendingEndpoint}?api_key=${BuildConfig.GIPHY_API_KEY}")
    fun getAllGifs(): Call<GifsListDataEntity>

    @GET("${GiphyCore.TrendingEndpoint}?api_key=${BuildConfig.GIPHY_API_KEY}&rating=g")
    fun getAllPopularGifs(): Call<GifsListDataEntity>

}