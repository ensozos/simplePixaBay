package com.zosimadis.domain

import com.zosimadis.data.ValidationResult

class ValidationException(val validationResult: ValidationResult) : Exception(validationResult.toString())
