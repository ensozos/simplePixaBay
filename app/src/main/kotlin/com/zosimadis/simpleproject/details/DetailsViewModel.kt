package com.zosimadis.simpleproject.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zosimadis.data.Result
import com.zosimadis.domain.GetImageDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getImageDetailsUseCase: GetImageDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val imageId: Long = checkNotNull(savedStateHandle["imageId"])

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<DetailsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadImageDetails()
    }

    private fun loadImageDetails() {
        viewModelScope.launch {
            getImageDetailsUseCase(imageId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                imageUrl = result.data.largeImageUrl,
                                imageSize = result.data.imageSize,
                                imageType = result.data.type,
                                tags = result.data.tags,
                                userName = result.data.user,
                                views = result.data.views,
                                likes = result.data.likes,
                                favorites = result.data.collections,
                                comments = result.data.comments,
                                downloads = result.data.downloads,
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _events.send(
                            DetailsEvent.ShowError(
                                result.exception.message ?: "Failed to load image details",
                            ),
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _events.close()
    }
}

sealed interface DetailsEvent {
    data class ShowError(val message: String) : DetailsEvent
}
