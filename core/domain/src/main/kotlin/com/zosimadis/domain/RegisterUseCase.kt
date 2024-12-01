package com.zosimadis.domain

import com.zosimadis.data.PixaBayRepository
import com.zosimadis.data.Result
import com.zosimadis.data.ValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: PixaBayRepository,
) {
    operator fun invoke(
        email: String,
        password: String,
        age: String,
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        val validation = validate(email, password, age)
        if (validation != null) {
            emit(Result.Error(ValidationException(validation)))
            return@flow
        }

        val ageInt = age.toInt()
        repository.register(email, password, ageInt).collect { result ->
            emit(result)
        }
    }

    private fun validate(
        email: String,
        password: String,
        age: String,
    ): ValidationResult? {
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

        val ageValue = age.toIntOrNull()
        when {
            ageValue == null -> {
                errors.add(ValidationResult.AgeError("Please enter your age"))
            }
            ageValue < ValidationConstants.MIN_AGE -> {
                errors.add(ValidationResult.AgeError("You must be at least ${ValidationConstants.MIN_AGE} years old"))
            }
            ageValue > ValidationConstants.MAX_AGE -> {
                errors.add(ValidationResult.AgeError("Age must be less than ${ValidationConstants.MAX_AGE} years"))
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
