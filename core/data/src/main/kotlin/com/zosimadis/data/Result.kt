package com.zosimadis.data


sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error<T>(val exception: Throwable) : Result<T>
    data object Loading : Result<Nothing>
}

sealed class ValidationResult {
    data class EmailError(val message: String) : ValidationResult()
    data class PasswordError(val message: String) : ValidationResult()
    data class AgeError(val message: String) : ValidationResult()
    data class MultipleErrors(val errors: List<ValidationResult>) : ValidationResult()
}