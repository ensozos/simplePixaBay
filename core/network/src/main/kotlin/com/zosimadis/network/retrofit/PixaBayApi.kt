package com.zosimadis.network.retrofit

import com.zosimadis.network.model.PixabayResponse
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface PixaBayApi {
    @GET(".")
    suspend fun getImages(
        @Query("key") apiKey: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
    ): PixabayResponse
}

@Serializable
internal data class NetworkResponse<T>(val data: T)
