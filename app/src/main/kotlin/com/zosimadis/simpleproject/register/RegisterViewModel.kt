package com.zosimadis.simpleproject.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zosimadis.data.Result
import com.zosimadis.data.ValidationResult
import com.zosimadis.domain.RegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<RegisterEvent>()
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

    fun updateAge(newAge: String) {
        _uiState.update {
            it.copy(
                age = newAge,
                ageError = null,
            )
        }
    }

    fun onRegisterClick() {
        // Reset all errors
        _uiState.update {
            it.copy(
                emailError = null,
                passwordError = null,
                ageError = null,
            )
        }

        viewModelScope.launch {
            val currentState = _uiState.value
            registerUseCase(currentState.email, currentState.password, currentState.age)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                        is Result.Success -> {
                            _uiState.update { it.copy(isLoading = false) }
                            _events.send(RegisterEvent.NavigateToHome)
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(isLoading = false) }
                            handleError(result.exception)
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
                            _events.send(RegisterEvent.ShowError(validationError.message))
                        }
                        is ValidationResult.PasswordError -> {
                            _uiState.update { it.copy(passwordError = validationError.message) }
                            _events.send(RegisterEvent.ShowError(validationError.message))
                        }
                        is ValidationResult.AgeError -> {
                            _uiState.update { it.copy(ageError = validationError.message) }
                            _events.send(RegisterEvent.ShowError(validationError.message))
                        }
                        is ValidationResult.MultipleErrors -> {
                            _uiState.update { state ->
                                var updatedState = state
                                validationError.errors.forEach { error ->
                                    updatedState = when (error) {
                                        is ValidationResult.EmailError -> updatedState.copy(emailError = error.message)
                                        is ValidationResult.PasswordError -> updatedState.copy(passwordError = error.message)
                                        is ValidationResult.AgeError -> updatedState.copy(ageError = error.message)
                                        is ValidationResult.MultipleErrors -> updatedState // shouldn't happen
                                    }
                                }
                                updatedState
                            }
                            _events.send(RegisterEvent.ShowError("Please fix all errors"))
                        }
                    }
                }
                else -> _events.send(RegisterEvent.ShowError("An unexpected error occurred"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _events.close()
    }
}

sealed interface RegisterEvent {
    data object NavigateToHome : RegisterEvent
    data class ShowError(val message: String) : RegisterEvent
}
