package com.example.uohih.joowon.model

data class SignUpFormState(val emailMsg: Int? = null,                   // 이메일
                           val passwordError: Int? = null,              // 비밀번호
                           val password2Error: Int? = null,             // 비밀번호 재입력
                           val isEmailOverlapValid: Boolean = false,    // 이메일 중복확인 버튼 활성화
                           val isDataValid: Boolean = false)            // 가입하기 버튼 활성화