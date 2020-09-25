package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.repository.signup.SignUpRepository

class SignUpViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                    signUpRepository = SignUpRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}