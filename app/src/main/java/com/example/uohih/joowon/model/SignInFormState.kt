package com.example.uohih.joowon.model

data class SignInFormState(val emailMsg: Int? = null,                   // 이메일
                           val passwordError: Int? = null,              // 비밀번호
                           val isDataValid: Boolean = false)
