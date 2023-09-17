package com.vd.study.data.remote.sources

import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.remote.exceptions.FailedLoadException
import com.vd.study.data.remote.exceptions.NotFoundException
import com.vd.study.data.remote.exceptions.TimeoutException
import com.vd.study.data.remote.exceptions.UnknownException
import retrofit2.Call
import javax.inject.Inject

class GifsApiDataSource @Inject constructor(
    private val gifsApiService: GifsApiService
) : GifsApiDataSourceBehavior {

    override suspend fun readGifs(): GifsListDataEntity {
        return executeReadingWithCall(gifsApiService.getAllGifs())
    }

    override suspend fun readPopularGifs(): GifsListDataEntity {
        return executeReadingWithCall(gifsApiService.getAllPopularGifs())
    }

    private fun <T> executeReadingWithCall(call: Call<T>): T {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                return response.body() ?: throw FailedLoadException()
            } else {
                throw getExceptionByCode(response.code())
            }
        } catch (e: Exception) {
            throw UnknownException()
        }
    }

    private fun getExceptionByCode(code: Int): Exception {
        return when (code) {
            404 -> NotFoundException()
            408 -> TimeoutException()
            else -> UnknownException()
        }
    }
}