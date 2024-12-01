package com.zosimadis.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zosimadis.data.PixaBayRepository
import com.zosimadis.data.Result
import com.zosimadis.data.mediator.PixabayRemoteMediator
import com.zosimadis.database.PixaBayDatabase
import com.zosimadis.database.model.ImageEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class DefaultPixaBayRepository @Inject constructor(
    private val database: PixaBayDatabase,
    private val remoteMediator: PixabayRemoteMediator,
) : PixaBayRepository {
    override fun register(email: String, password: String, age: Int): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        // network delay
        delay(1000)

        // Mock successful registration or error here
        // here in a production app we should save the id and session token to the local storage (data storage)
        emit(Result.Success(Unit))
    }

    override fun login(username: String, password: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        delay(1000)

        // Mock successful login
        // here in a production app we should update the id and session token to the local storage (data storage)
        emit(Result.Success(Unit))
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getImages(): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE,
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.imageDao().getPagingSource() },
        ).flow
    }

    override fun getImageById(imageId: Long): Flow<Result<ImageEntity>> = flow {
        emit(Result.Loading)

        try {
            val image = database.imageDao().getImageById(imageId)
            if (image != null) {
                emit(Result.Success(image))
            } else {
                emit(Result.Error(Exception("Image not found")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}
