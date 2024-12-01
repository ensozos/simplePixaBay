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

class LoginUseCaseTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository = FakePixaBayRepository()
    private val loginUseCase = LoginUseCase(repository)

    @Test
    fun `when email is empty, should return email validation error`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            loginUseCase("", "valid123").toList(emittedValues)
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
            loginUseCase("invalid-email", "valid123").toList(emittedValues)
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
            loginUseCase("test@test.com", "").toList(emittedValues)
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
            loginUseCase("test@test.com", "12345").toList(emittedValues)
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
            loginUseCase("test@test.com", "123456789012345").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
        assert((emittedValues[1] as Result.Error).exception is ValidationException)
        assert(((emittedValues[1] as Result.Error).exception as ValidationException).validationResult is ValidationResult.PasswordError)
    }

    @Test
    fun `when credentials are valid, should return success`() = runTest {
        val emittedValues = mutableListOf<Result<Unit>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            loginUseCase("test@test.com", "valid123").toList(emittedValues)
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
            loginUseCase("test@test.com", "valid123").toList(emittedValues)
        }

        assert(emittedValues.size == 2)
        assert(emittedValues[0] is Result.Loading)
        assert(emittedValues[1] is Result.Error)
    }
}
