package com.zosimadis.domain

import com.zosimadis.data.PixaBayRepository
import com.zosimadis.data.Result
import com.zosimadis.database.model.ImageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageDetailsUseCase @Inject constructor(
    private val repository: PixaBayRepository,
) {
    operator fun invoke(imageId: Long): Flow<Result<ImageEntity>> = repository.getImageById(imageId)
}
