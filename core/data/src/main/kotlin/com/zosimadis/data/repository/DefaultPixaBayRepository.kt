package com.zosimadis.data.repository

import android.content.SharedPreferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zosimadis.data.PixaBayRepository
import com.zosimadis.data.Result
import com.zosimadis.data.mediator.PixabayRemoteMediator
import com.zosimadis.database.PixaBayDatabase
import com.zosimadis.database.model.ImageEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class DefaultPixaBayRepository @Inject constructor(
    private val database: PixaBayDatabase,
    private val remoteMediator: PixabayRemoteMediator,
    private val sharedPreferences: SharedPreferences,
) : PixaBayRepository {

    override fun isLoggedIn(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val sessionToken = sharedPreferences.getString(KEY_SESSION_TOKEN, null)
            val sessionTimestamp = sharedPreferences.getLong(KEY_SESSION_TIMESTAMP, 0)
            val userEmail = sharedPreferences.getString(KEY_USER_EMAIL, null)

            if (sessionToken != null && userEmail != null && !isSessionExpired(sessionTimestamp)) {
                emit(Result.Success(Unit))
            } else {
                clearSession()
                emit(Result.Error(Exception("Not logged in")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun register(email: String, password: String, age: Int): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        // network delay
        delay(1000)

        // Mock successful registration or error here
        // here in a production app we should save the id and session token to the local storage (data storage)
        emit(Result.Success(Unit))
    }

    override fun login(email: String, password: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        
        // Simulate network delay
        delay(1000)

        // Mock successful login response
        val mockSessionToken = "session_${System.currentTimeMillis()}"
        val sessionTimestamp = System.currentTimeMillis()

        // Save session data
        sharedPreferences.edit()
            .putString(KEY_SESSION_TOKEN, mockSessionToken)
            .putLong(KEY_SESSION_TIMESTAMP, sessionTimestamp)
            .putString(KEY_USER_EMAIL, email)
            .apply()


        //TODO use the token in your requests
        emit(Result.Success(Unit))
    }

    private fun isSessionExpired(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val sessionAge = currentTime - timestamp
        return sessionAge > SESSION_DURATION
    }

    private fun clearSession() {
        sharedPreferences.edit()
            .remove(KEY_SESSION_TOKEN)
            .remove(KEY_SESSION_TIMESTAMP)
            .remove(KEY_USER_EMAIL)
            .apply()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getImages(): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE,
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.imageDao().getPagingSource() },
        ).flow
    }

    override fun getImageById(imageId: Long): Flow<Result<ImageEntity>> = flow {
        emit(Result.Loading)

        try {
            val image = database.imageDao().getImageById(imageId)
            if (image != null) {
                emit(Result.Success(image))
            } else {
                emit(Result.Error(Exception("Image not found")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
        private const val KEY_SESSION_TOKEN = "session_token"
        private const val KEY_SESSION_TIMESTAMP = "session_timestamp"
        private const val KEY_USER_EMAIL = "user_email"
        private const val SESSION_DURATION = 24 * 60 * 60 * 1000L
    }
}
