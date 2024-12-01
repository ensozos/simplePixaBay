package com.zosimadis.database.di

import android.content.Context
import androidx.room.Room
import com.zosimadis.database.PixaBayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): PixaBayDatabase = Room.databaseBuilder(
        context,
        PixaBayDatabase::class.java,
        "pixabay_database",
    ).build()
}
