package com.zosimadis.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val imageId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
)
