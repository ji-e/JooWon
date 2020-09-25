package com.example.uohih.joowon.model.signup

data class SignUpFormState(val usernameError: Int? = null,
                           val passwordError: Int? = null,
                           val password2Error: Int? = null,
                           val isDataValid: Boolean = false)

data class SignUpResult(val success: Int? = null,
                        val error: Int? = null)
