package com.zosimadis.database.di

import com.zosimadis.database.PixaBayDatabase
import com.zosimadis.database.dao.ImageDao
import com.zosimadis.database.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideBookDao(
        db: PixaBayDatabase,
    ): ImageDao = db.imageDao()

    @Provides
    fun provideRemoteKeysDao(
        db: PixaBayDatabase,
    ): RemoteKeysDao = db.remoteKeysDao()
}
