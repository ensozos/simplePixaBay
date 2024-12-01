package com.zosimadis.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkImageResource(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    @SerialName("previewURL")
    val previewUrl: String,
    @SerialName("previewWidth")
    val previewWidth: Int,
    @SerialName("previewHeight")
    val previewHeight: Int,
    @SerialName("webformatURL")
    val webformatUrl: String,
    @SerialName("webformatWidth")
    val webformatWidth: Int,
    @SerialName("webformatHeight")
    val webformatHeight: Int,
    @SerialName("largeImageURL")
    val largeImageUrl: String,
    @SerialName("imageSize")
    val imageSize: Long,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    @SerialName("user_id")
    val userId: Int,
    val user: String,
    @SerialName("userImageURL")
    val userImageUrl: String,
)

@Serializable
data class PixabayResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<NetworkImageResource>,
)
