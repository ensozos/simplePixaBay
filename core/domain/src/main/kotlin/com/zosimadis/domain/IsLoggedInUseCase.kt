package com.zosimadis.domain

import com.zosimadis.data.PixaBayRepository
import com.zosimadis.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val repository: PixaBayRepository,
) {
    operator fun invoke(): Flow<Result<Unit>> = repository.isLoggedIn()
        .catch { error -> 
            emit(Result.Error(error))
        }
}