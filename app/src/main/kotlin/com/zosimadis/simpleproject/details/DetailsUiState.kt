package com.zosimadis.simpleproject.details

data class DetailsUiState(
    val isLoading: Boolean = true,
    val imageUrl: String = "",
    val imageSize: Long = 0,
    val imageType: String = "",
    val tags: String = "",
    val userName: String = "",
    val views: Int = 0,
    val likes: Int = 0,
    val favorites: Int = 0,
    val comments: Int = 0,
    val downloads: Int = 0,
    val error: String? = null,
)
