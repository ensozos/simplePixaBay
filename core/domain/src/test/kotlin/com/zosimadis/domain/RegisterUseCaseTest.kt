package com.zosimadis.domain

import com.zosimadis.data.Result
import com.zosimadis.data.ValidationResult
import com.zosimadis.testing.repository.FakePixaBayRepository
import com.zosimadis.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class RegisterUseCaseTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository = FakePixaBayRepository()
    private val registerUseCase = RegisterUseCase(repository)

    @Test
    fun `when email is empty, should return email validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("", "valid123", "25").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.EmailError)
    }

    @Test
    fun `when email is invalid format, should return email validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("invalid-email", "valid123", "25").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.EmailError)
    }

    @Test
    fun `when password is empty, should return password validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("test@test.com", "", "25").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.PasswordError)
    }

    @Test
    fun `when password is too short, should return password validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("test@test.com", "12345", "25").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.PasswordError)
    }

    @Test
    fun `when password is too long, should return password validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("test@test.com", "123456789012345", "25").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.PasswordError)
    }

    @Test
    fun `when age is below minimum, should return age validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("test@test.com", "valid123", "17").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.AgeError)
    }

    @Test
    fun `when age is above maximum, should return age validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("test@test.com", "valid123", "100").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.AgeError)
    }

    @Test
    fun `when all inputs are valid, should return success`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("test@test.com", "valid123", "25").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Success)
    }

    @Test
    fun `when repository returns error, should propagate error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()
        repository.setReturnError(true)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            registerUseCase("test@test.com", "valid123", "25").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
    }
}
