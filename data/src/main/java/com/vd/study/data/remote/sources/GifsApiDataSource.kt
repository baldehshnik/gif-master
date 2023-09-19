package com.vd.study.data.remote.sources

import com.vd.study.data.remote.entities.GifsListDataEntity
import com.vd.study.data.exceptions.FailedLoadException
import com.vd.study.data.exceptions.NotFoundException
import com.vd.study.data.exceptions.TimeoutException
import com.vd.study.data.exceptions.UnknownException
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
        val response = try {
            call.execute()
        } catch (e: Exception) {
            throw UnknownException()
        }

        if (response.isSuccessful) {
            return response.body() ?: throw FailedLoadException()
        } else {
            throw getExceptionByCode(response.code())
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