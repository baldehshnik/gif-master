package com.vd.study.home.domain.repositories

import com.vd.study.home.domain.entities.GifEntity
import com.vd.study.core.Result
import com.vd.study.home.domain.entities.LikeAndSaveStatusEntity

interface HomeRepository {

    suspend fun readGifs(): Result<GifEntity>

    suspend fun readLikeAndSaveStatus(accountId: Int, gif: GifEntity): Result<LikeAndSaveStatusEntity>

}