package com.example.uohih.joowon.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.repository.JWBaseDataSource
import com.example.uohih.joowon.repository.JWBaseRepository

class MainListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainListViewModel::class.java)) {
            return MainListViewModel(
                    application = JWBaseApplication(),
                    jwBaseRepository = JWBaseRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}