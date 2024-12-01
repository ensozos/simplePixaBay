package com.zosimadis.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zosimadis.database.model.ImageEntity

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageEntity>)

    @Query("SELECT * FROM images ORDER BY time_stamp ASC")
    fun getPagingSource(): PagingSource<Int, ImageEntity>

    @Query("SELECT * FROM images WHERE localId = :imageId")
    suspend fun getImageById(imageId: Long): ImageEntity?

    @Query("DELETE FROM images")
    suspend fun clearAll()
}
