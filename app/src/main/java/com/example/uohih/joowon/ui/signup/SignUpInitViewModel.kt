package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.signup.SU1002
import com.example.uohih.joowon.model.signup.SU1003
import com.example.uohih.joowon.repository.signup.SignUpRepository
import com.google.gson.JsonObject

class SignUpInitViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {
    private val _su1002Data = MutableLiveData<SU1002>()
    val su1002Data: LiveData<SU1002> = _su1002Data


    fun su1003(accessToken: String) {
        signUpRepository.su1003(accessToken, object : SignUpRepository.GetResbodyCallback<SU1003> {
            override fun onSuccess(data: SU1003){
                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", "SU1002")
                jsonObject.addProperty("email", data.resbody?.email)
                jsonObject.addProperty("sns_id",data.resbody?.id)
                jsonObject.addProperty("sns_provider",data.resbody.toString())
                su1002(jsonObject)
            }
            override fun onFailure(code: Int) {
             LogUtil.e(code)
            }

            override fun onError(throwable: Throwable) {
                }

        })

    }

    fun su1002(jsonObject: JsonObject){
        signUpRepository.su1002(jsonObject, object : SignUpRepository.GetResbodyCallback<SU1002> {
            override fun onSuccess(data: SU1002){
                _su1002Data.value = (data)
            }
            override fun onFailure(code: Int) {
                LogUtil.e(code)
            }

            override fun onError(throwable: Throwable) {
            }

        })
    }

}