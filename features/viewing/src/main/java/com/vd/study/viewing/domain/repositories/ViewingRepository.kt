package com.vd.study.viewing.domain.repositories

import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.core.container.Result

interface ViewingRepository {

    suspend fun updateGif(gif: GifEntity): Result<Boolean>

}