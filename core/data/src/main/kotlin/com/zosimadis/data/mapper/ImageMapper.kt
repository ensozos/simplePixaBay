package com.zosimadis.data.mapper

import com.zosimadis.database.model.ImageEntity
import com.zosimadis.network.model.NetworkImageResource

fun NetworkImageResource.toImageEntity(): ImageEntity {
    return ImageEntity(
        id = id,
        pageUrl = pageURL,
        type = type,
        tags = tags,
        previewUrl = previewUrl,
        previewWidth = previewWidth,
        previewHeight = previewHeight,
        webformatUrl = webformatUrl,
        webformatWidth = webformatWidth,
        webformatHeight = webformatHeight,
        largeImageUrl = largeImageUrl,
        views = views,
        downloads = downloads,
        likes = likes,
        comments = comments,
        userId = userId,
        user = user,
        imageSize = imageSize,
        userImageUrl = userImageUrl,
        collections = collections
    )
}

fun ImageEntity.toNetworkResource(): NetworkImageResource {
    return NetworkImageResource(
        id = id,
        pageURL = pageUrl,
        type = type,
        tags = tags,
        previewUrl = previewUrl,
        previewWidth = previewWidth,
        previewHeight = previewHeight,
        webformatUrl = webformatUrl,
        webformatWidth = webformatWidth,
        webformatHeight = webformatHeight,
        largeImageUrl = largeImageUrl,
        views = views,
        downloads = downloads,
        likes = likes,
        comments = comments,
        userId = userId,
        user = user,
        userImageUrl = userImageUrl,
        imageSize = imageSize,
        collections = collections,
    )
}
