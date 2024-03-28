package com.vd.study.data.remote.sources

import com.vd.study.data.BuildConfig
import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.remote.entities.GiphyCore
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val MAX_ONCE_LOADING_GIFS_COUNT = 15
const val MAX_ELEMENTS_COUNT = 4999

interface GifsApiService {

    @GET("${GiphyCore.TrendingEndpoint}?api_key=${BuildConfig.GIPHY_API_KEY}")
    fun getAllGifs(): Call<GifsListDataEntity>

    @GET("${GiphyCore.TrendingEndpoint}?api_key=${BuildConfig.GIPHY_API_KEY}&rating=g")
    fun getAllPopularGifs(): Call<GifsListDataEntity>

    @GET("${GiphyCore.TrendingEndpoint}?api_key=${BuildConfig.GIPHY_API_KEY}")
    fun readGifs(@Query("offset") offset: Int): Call<GifsListDataEntity>

    @GET("${GiphyCore.SearchEndpoint}?api_key=${BuildConfig.GIPHY_API_KEY}")
    fun readSearchGifs(
        @Query("q") q: String,
        @Query("offset") offset: Int
    ): Call<GifsListDataEntity>

}