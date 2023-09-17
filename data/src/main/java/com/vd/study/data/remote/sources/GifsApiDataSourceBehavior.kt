package com.vd.study.data.remote.sources

import com.vd.study.data.remote.entities.GifsListDataEntity

interface GifsApiDataSourceBehavior {

    suspend fun readGifs(): GifsListDataEntity

    suspend fun readPopularGifs(): GifsListDataEntity

}