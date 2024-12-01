package com.zosimadis.domain

import com.zosimadis.data.Result
import com.zosimadis.database.model.ImageEntity
import com.zosimadis.testing.repository.FakePixaBayRepository
import com.zosimadis.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GetImageDetailsUseCaseTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository = FakePixaBayRepository()
    private val getImageDetailsUseCase = GetImageDetailsUseCase(repository)

    @Test
    fun `when repository returns success, should emit success with image details`() = runTest {
        val testImage = fakeImage
        repository.setImageToReturn(testImage)

        val emittedValues = mutableListOf<Result<ImageEntity>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            getImageDetailsUseCase(123L).toList(emittedValues)
        }

        assert(emittedValues.size == 1)
        assert(emittedValues[0] is Result.Success)
        assert((emittedValues[0] as Result.Success).data == testImage)
    }

    @Test
    fun `when repository returns error, should emit error`() = runTest {
        repository.setReturnError(true)

        val emittedValues = mutableListOf<Result<ImageEntity>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            getImageDetailsUseCase(123L).toList(emittedValues)
        }

        assert(emittedValues.size == 1)
        assert(emittedValues[0] is Result.Error)
    }

    @Test
    fun `when image id not found, should emit error`() = runTest {
        repository.setImageToReturn(null)

        val emittedValues = mutableListOf<Result<ImageEntity>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            getImageDetailsUseCase(999L).toList(emittedValues)
        }

        assert(emittedValues.size == 1)
        assert(emittedValues[0] is Result.Error)
    }
}

private val fakeImage = ImageEntity(
    localId = 1L,
    id = 123,
    pageUrl = "test-url",
    type = "photo",
    tags = "test, image",
    previewUrl = "preview-url",
    previewWidth = 150,
    previewHeight = 100,
    webformatUrl = "webformat-url",
    webformatWidth = 640,
    webformatHeight = 480,
    largeImageUrl = "large-url",
    imageSize = 1024000,
    views = 1000,
    downloads = 500,
    likes = 100,
    comments = 25,
    userId = 123,
    user = "test-user",
    userImageUrl = "user-image-url",
)
