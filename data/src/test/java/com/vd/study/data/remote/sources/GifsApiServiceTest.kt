package com.vd.study.data.remote.sources

import com.vd.study.data.remote.entities.GiphyCore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GifsApiServiceTest {

    @Test
    fun readGifsWithPaging_AfterGetRequest_Return50GifsAnd50MoreAfter() {
        val retrofit = createRetrofit()

        val call = retrofit.readGifs(0)
        val data = call.execute().body()

        val call2 = retrofit.readGifs(50)
        val data2 = call2.execute().body()

        assertEquals(true, call.isExecuted)
        assertNotEquals(null, data)
        assertEquals(50, data!!.data.size)

        assertEquals(true, call2.isExecuted)
        assertNotEquals(null, data2)
        assertEquals(50, data2!!.data.size)

        assertNotEquals(data2.data[16], data.data[16])
    }

    @Test
    fun readGifs_AfterGetRequest_Return50Gifs() {
        val retrofit = createRetrofit()

        val call = retrofit.getAllGifs()
        val data = call.execute().body()

        assertEquals(true, call.isExecuted)
        assertNotEquals(null, data)
        assertEquals(50, data!!.data.size)
    }

    @Test
    fun readGifs_AfterGetRequest_Return50PopularGifs() {
        val retrofit = createRetrofit()

        val call = retrofit.getAllPopularGifs()
        val data = call.execute().body()

        assertEquals(true, call.isExecuted)
        assertNotEquals(null, data)
        assertEquals(50, data!!.data.size)
        data.data.forEach {
            assertEquals("g", it.rating)
        }
    }

    private fun createRetrofit(): GifsApiService {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(GiphyCore.BASE_GIFS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitBuilder.create(GifsApiService::class.java)
    }
}