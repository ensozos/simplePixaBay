package com.zosimadis.simpleproject.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zosimadis.data.PixaBayRepository
import com.zosimadis.database.model.ImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PixaBayRepository,
) : ViewModel() {

    val imagesFlow: Flow<PagingData<ImageEntity>> = repository
        .getImages()
        .cachedIn(viewModelScope)
}
