package com.zosimadis.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.zosimadis.data.mapper.toImageEntity
import com.zosimadis.database.PixaBayDatabase
import com.zosimadis.database.model.ImageEntity
import com.zosimadis.database.model.RemoteKeysEntity
import com.zosimadis.network.PixaBayDataSource
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PixabayRemoteMediator @Inject constructor(
    private val pixaBayDataSource: PixaBayDataSource,
    private val pixaBayDatabase: PixaBayDatabase,
) : RemoteMediator<Int, ImageEntity>() {

    private val imageDao = pixaBayDatabase.imageDao()
    private val remoteKeysDao = pixaBayDatabase.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>,
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                        ?: return MediatorResult.Success(endOfPaginationReached = false)
                    val nextKey = remoteKeys.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextKey
                }
            }

            if (loadType == LoadType.APPEND && page == STARTING_PAGE_INDEX) {
                return MediatorResult.Success(endOfPaginationReached = false)
            }

            val images = pixaBayDataSource.getImages(
                page = page,
                perPage = state.config.pageSize,
            )

            val endOfPaginationReached = images.isEmpty()

            pixaBayDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    imageDao.clearAll()
                }

                val prevKey = if (page > STARTING_PAGE_INDEX) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1

                val remoteKeys = images.map { image ->
                    RemoteKeysEntity(
                        imageId = image.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                    )
                }

                remoteKeysDao.insertAll(remoteKeys)
                imageDao.insertAll(images.map { it.toImageEntity() })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            println("Error in RemoteMediator: ${e.message}")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ImageEntity>,
    ): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { image ->
                remoteKeysDao.remoteKeysImageId(image.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ImageEntity>,
    ): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { image ->
                remoteKeysDao.remoteKeysImageId(image.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ImageEntity>,
    ): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.remoteKeysImageId(id)
            }
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
