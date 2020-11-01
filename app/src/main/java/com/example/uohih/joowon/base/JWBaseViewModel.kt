package com.example.uohih.joowon.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.repository.JWBaseRepository

open class JWBaseViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : AndroidViewModel(application) {
     val _isNetworkErr = MutableLiveData<Boolean>()

    val isNetworkErr: LiveData<Boolean> = _isNetworkErr
}