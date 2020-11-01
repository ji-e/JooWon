package com.example.uohih.joowon.ui.setting

import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.regex.Pattern

class SettingViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : JWBaseViewModel(application, jwBaseRepository) {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _jw2002Data = MutableLiveData<JW2002>()

    val isLoading: LiveData<Boolean> = _isLoading
    val jw2002Data: LiveData<JW2002> = _jw2002Data  // 로그아웃


    /**
     * 로그아웃
     * jw2002
     */
    fun signOut(jsonObject: JsonObject) {
        _isLoading.value = true
        JWBaseRepository().requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                val jw2002Data = Gson().fromJson(resbodyData.toString(), JW2002::class.java)
                _jw2002Data.value = jw2002Data
                _isLoading.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoading.value = false
                _isNetworkErr.value = true
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }

        })
    }
}