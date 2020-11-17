package com.example.uohih.joowon.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class JWBaseViewModel() : ViewModel() {
    val _isLoading = MutableLiveData<Boolean>()
    val _isNetworkErr = MutableLiveData<Boolean>()

    val isNetworkErr: LiveData<Boolean> = _isNetworkErr
    val isLoading: LiveData<Boolean> = _isLoading
}