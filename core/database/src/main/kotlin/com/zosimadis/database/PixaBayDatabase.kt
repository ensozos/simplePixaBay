package com.zosimadis.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zosimadis.database.dao.ImageDao
import com.zosimadis.database.dao.RemoteKeysDao
import com.zosimadis.database.model.ImageEntity
import com.zosimadis.database.model.RemoteKeysEntity

@Database(
    entities = [
        ImageEntity::class,
        RemoteKeysEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class PixaBayDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
