package com.example.uohih.joowon.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.repository.JWBaseRepository

class SettingViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(
                    application = JWBaseApplication(),
                    jwBaseRepository = JWBaseRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}