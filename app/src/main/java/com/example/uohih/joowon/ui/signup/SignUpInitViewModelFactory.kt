package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.repository.signup.SignUpDataSource
import com.example.uohih.joowon.repository.signup.SignUpRepository

class SignUpInitViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpInitViewModel::class.java)) {
            return SignUpInitViewModel(
                    signUpRepository = SignUpRepository(dataSource = SignUpDataSource())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}