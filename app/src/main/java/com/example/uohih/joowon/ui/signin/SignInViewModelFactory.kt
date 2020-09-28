package com.example.uohih.joowon.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.repository.signin.SignInDataSource
import com.example.uohih.joowon.repository.signin.SignInRepository
import com.example.uohih.joowon.repository.signup.SignUpDataSource
import com.example.uohih.joowon.repository.signup.SignUpRepository

class SignInViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(
                    signInRepository = SignInRepository(dataSource = SignInDataSource())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}