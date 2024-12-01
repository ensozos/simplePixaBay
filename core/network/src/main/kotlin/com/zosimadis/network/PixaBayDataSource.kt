package com.zosimadis.network

import com.zosimadis.network.model.NetworkImageResource

interface PixaBayDataSource {
    suspend fun getImages(page: Int, perPage: Int): List<NetworkImageResource>
}
