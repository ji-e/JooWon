package com.example.uohih.joowon.ui.main

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.model.JW3001
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

class MainListViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : AndroidViewModel(application) {
    private val _isLoading = MutableLiveData<Boolean>()
    private var _jw3001Data = MutableLiveData<JW3001>()

    val isLoading: LiveData<Boolean> = _isLoading
    val jw3001Data: LiveData<JW3001> = _jw3001Data


    /**
     * 직원리스트 가져오기
     * jw3001
     */
    fun getEmployeeList(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestBaseService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                _isLoading.value = false

                val jw3001Data = Gson().fromJson(data.toString(), JW3001::class.java)

                if ("false" == jw3001Data.result) {
                    return
                }
                if ("N" == jw3001Data.resbody?.successYn) {
                    return
                }

                _jw3001Data.value = jw3001Data
            }

            override fun onFailure(code: Int) {
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
            }
        })
    }

}