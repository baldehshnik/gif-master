package com.vd.study.data

import com.vd.study.core.Result
import com.vd.study.data.remote.entities.GifsListDataEntity

interface RemoteGifsDataRepository {

    suspend fun readGifs(): Result<GifsListDataEntity>

    suspend fun readPopularGifs(): Result<GifsListDataEntity>

}