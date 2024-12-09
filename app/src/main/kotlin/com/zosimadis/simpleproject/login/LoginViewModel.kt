package com.zosimadis.simpleproject.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zosimadis.data.Result
import com.zosimadis.data.ValidationResult
import com.zosimadis.domain.IsLoggedInUseCase
import com.zosimadis.domain.LoginUseCase
import com.zosimadis.domain.ValidationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update {
            it.copy(
                email = newEmail,
                emailError = null,
            )
        }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update {
            it.copy(
                password = newPassword,
                passwordError = null,
            )
        }
    }

    fun onLoginClick() {
        // Reset errors
        _uiState.update {
            it.copy(
                emailError = null,
                passwordError = null,
            )
        }

        viewModelScope.launch {
            val currentState = _uiState.value
            var shouldProceedWithLogin = true

            isLoggedInUseCase().collect { result ->
                when(result) {
                    is Result.Success -> {
                        shouldProceedWithLogin = false
                        _events.send(LoginEvent.NavigateToHome)
                    }
                    is Result.Error -> {

                    }
                    Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }

            if (shouldProceedWithLogin) {
                loginUseCase(currentState.email, currentState.password)
                    .collect { loginResult ->
                        when (loginResult) {
                            is Result.Loading -> {
                                _uiState.update { it.copy(isLoading = true) }
                            }
                            is Result.Success -> {
                                _uiState.update { it.copy(isLoading = false) }
                                _events.send(LoginEvent.NavigateToHome)
                            }
                            is Result.Error -> {
                                _uiState.update { it.copy(isLoading = false) }
                                handleError(loginResult.exception)
                            }
                        }
                    }
            }
        }
    }

    private fun handleError(error: Throwable) {
        viewModelScope.launch {
            when (error) {
                is ValidationException -> {
                    when (val validationError = error.validationResult) {
                        is ValidationResult.EmailError -> {
                            _uiState.update { it.copy(emailError = validationError.message) }
                            _events.send(LoginEvent.ShowError(validationError.message))
                        }
                        is ValidationResult.PasswordError -> {
                            _uiState.update { it.copy(passwordError = validationError.message) }
                            _events.send(LoginEvent.ShowError(validationError.message))
                        }
                        is ValidationResult.MultipleErrors -> {
                            _uiState.update { state ->
                                var updatedState = state
                                validationError.errors.forEach { error ->
                                    updatedState = when (error) {
                                        is ValidationResult.EmailError -> updatedState.copy(emailError = error.message)
                                        is ValidationResult.PasswordError -> updatedState.copy(passwordError = error.message)
                                        else -> updatedState // Other cases shouldn't happen for login
                                    }
                                }
                                updatedState
                            }
                            _events.send(LoginEvent.ShowError("Please fix all errors"))
                        }
                        else -> _events.send(LoginEvent.ShowError("An unexpected error occurred"))
                    }
                }
                else -> _events.send(LoginEvent.ShowError("An unexpected error occurred"))
            }
        }
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            isLoggedInUseCase().collect { result ->
                println("checkLoginStatus: $result")
                when(result) {
                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _events.send(LoginEvent.NavigateToHome)
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
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

sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
    data class ShowError(val message: String) : LoginEvent
}
