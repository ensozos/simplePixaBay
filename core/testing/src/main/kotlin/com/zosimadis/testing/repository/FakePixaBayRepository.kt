package com.zosimadis.testing.repository

import com.zosimadis.data.PixaBayRepository
import com.zosimadis.data.Result
import com.zosimadis.database.model.ImageEntity
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class FakePixaBayRepository : PixaBayRepository {

    private var shouldReturnError = false
    private val loginResultFlow = MutableSharedFlow<Result<Unit>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    private val registerResultFlow = MutableSharedFlow<Result<Unit>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    private var imageToReturn: ImageEntity? = null

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setImageToReturn(image: ImageEntity?) {
        imageToReturn = image
    }

    override fun login(email: String, password: String): Flow<Result<Unit>> = flow {
        if (shouldReturnError) {
            emit(Result.Error(Exception("Test error")))
        } else {
            emit(Result.Success(Unit))
        }
    }

    override fun register(email: String, password: String, age: Int): Flow<Result<Unit>> = flow {
        if (shouldReturnError) {
            emit(Result.Error(Exception("Registration failed")))
        } else {
            emit(Result.Success(Unit))
        }
    }

    fun sendLoginResult(result: Result<Unit>) {
        loginResultFlow.tryEmit(result)
    }

    fun sendRegisterResult(result: Result<Unit>) {
        registerResultFlow.tryEmit(result)
    }

    override fun getImages() = throw NotImplementedError("Not implemented for tests")
    override fun getImageById(imageId: Long): Flow<Result<ImageEntity>> = flow {
        if (shouldReturnError) {
            emit(Result.Error(Exception("Error fetching image")))
            return@flow
        }

        imageToReturn?.let {
            emit(Result.Success(it))
        } ?: emit(Result.Error(Exception("Image not found")))
    }
}
