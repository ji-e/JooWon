package com.example.uohih.joowon.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.repository.JWBaseDataSource
import com.example.uohih.joowon.repository.JWBaseRepository

class SignInViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(
                    jwBaseRepository = JWBaseRepository(dataSource = JWBaseDataSource())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}