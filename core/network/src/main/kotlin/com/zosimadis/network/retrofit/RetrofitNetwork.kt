package com.zosimadis.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zosimadis.network.PixaBayDataSource
import com.zosimadis.network.di.ApiKey
import com.zosimadis.network.model.NetworkImageResource
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://pixabay.com/api/"

@Singleton
internal class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
    @ApiKey private val apiKey: String,
) : PixaBayDataSource {

    private val pixaBayApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(PixaBayApi::class.java)

    override suspend fun getImages(page: Int, perPage: Int): List<NetworkImageResource> {
        return pixaBayApi.getImages(
            apiKey = apiKey,
            page = page,
            perPage = perPage,
        ).hits
    }
}
