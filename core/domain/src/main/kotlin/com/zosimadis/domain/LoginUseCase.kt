package com.zosimadis.domain

import com.zosimadis.data.PixaBayRepository
import com.zosimadis.data.Result
import com.zosimadis.data.ValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: PixaBayRepository,
) {
    operator fun invoke(email: String, password: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        val validation = validate(email, password)
        if (validation != null) {
            emit(Result.Error(ValidationException(validation)))
            return@flow
        }

        repository.login(email, password).collect { result ->
            emit(result)
        }
    }

    private fun validate(email: String, password: String): ValidationResult? {
        val errors = mutableListOf<ValidationResult>()

        if (!isValidEmail(email)) {
            errors.add(ValidationResult.EmailError("Please enter a valid email"))
        }

        when {
            password.isEmpty() -> {
                errors.add(ValidationResult.PasswordError("Password cannot be empty"))
            }
            password.length < ValidationConstants.MIN_PASSWORD_LENGTH -> {
                errors.add(ValidationResult.PasswordError("Password must be at least ${ValidationConstants.MIN_PASSWORD_LENGTH} characters"))
            }
            password.length > ValidationConstants.MAX_PASSWORD_LENGTH -> {
                errors.add(ValidationResult.PasswordError("Password must be less than ${ValidationConstants.MAX_PASSWORD_LENGTH} characters"))
            }
        }

        return when {
            errors.isEmpty() -> null
            errors.size == 1 -> errors.first()
            else -> ValidationResult.MultipleErrors(errors)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.isNotEmpty() && email.matches(emailRegex.toRegex())
    }
}
