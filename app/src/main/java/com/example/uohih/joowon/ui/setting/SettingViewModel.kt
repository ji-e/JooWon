package com.example.uohih.joowon.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.data.setting.SettingRepository
import com.google.gson.JsonObject

class SettingViewModel(private val settingRepository: SettingRepository) : JWBaseViewModel() {

    private val _jw2002Data = MutableLiveData<JW2002>()

    val jw2002Data: LiveData<JW2002> = _jw2002Data  // 로그아웃


    /**
     * 로그아웃
     * jw2002
     */
    fun signOut(jsonObject: JsonObject) {
        _isLoading.value = true
        settingRepository.signOut(jsonObject = jsonObject,
                success = {
                    _jw2002Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                })
    }
}