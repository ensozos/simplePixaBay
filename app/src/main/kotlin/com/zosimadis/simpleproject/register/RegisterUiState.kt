package com.zosimadis.simpleproject.register

data class RegisterUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val age: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val ageError: String? = null,
)
