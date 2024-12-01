package com.zosimadis.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.atomic.AtomicLong

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0L,
    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long = TIMESTAMP_COUNTER.getAndIncrement(),
    val id: Int,
    val pageUrl: String,
    val type: String,
    val tags: String,
    val previewUrl: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val imageSize: Long,
    val webformatUrl: String,
    val webformatWidth: Int,
    val webformatHeight: Int,
    val largeImageUrl: String,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val userId: Int,
    val user: String,
    val userImageUrl: String,
) {
    companion object {
        private val TIMESTAMP_COUNTER = AtomicLong(System.currentTimeMillis())
    }
}
