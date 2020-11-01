package com.example.uohih.joowon.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.repository.JWBaseDataSource
import com.example.uohih.joowon.repository.JWBaseRepository

class JWBaseViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JWBaseViewModel::class.java)) {
            return JWBaseViewModel(
                    application = JWBaseApplication(),
                    jwBaseRepository = JWBaseRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}