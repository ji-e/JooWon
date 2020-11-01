package com.example.uohih.joowon.ui.vacation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.repository.JWBaseRepository

class VacationViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VacationViewModel::class.java)) {
            return VacationViewModel(
                    application = JWBaseApplication(),
                    jwBaseRepository = JWBaseRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}