package com.zosimadis.data

import androidx.paging.PagingData
import com.zosimadis.database.model.ImageEntity
import kotlinx.coroutines.flow.Flow

interface PixaBayRepository {
    fun login(email: String, password: String): Flow<Result<Unit>>
    fun register(email: String, password: String, age: Int): Flow<Result<Unit>>
    fun getImages(): Flow<PagingData<ImageEntity>>
    fun getImageById(imageId: Long): Flow<Result<ImageEntity>>
}
