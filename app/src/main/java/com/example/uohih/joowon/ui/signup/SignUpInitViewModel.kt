package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.JW1001
import com.example.uohih.joowon.model.JW1002
import com.example.uohih.joowon.repository.signup.SignUpRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

class SignUpInitViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {
    private val gson = Gson()

    private val _su1002Data = MutableLiveData<JW1002>()
    val JW1002Data: LiveData<JW1002> = _su1002Data


//    fun su1003(accessToken: String) {
//        signUpRepository.su1003(accessToken, object : SignUpRepository.GetResbodyCallback<JW1003> {
//            override fun onSuccess(data: JW1003){
//                val jsonObject = JsonObject()
//                jsonObject.addProperty("methodid", "SU1002")
//                jsonObject.addProperty("email", data.resbody?.email)
//                jsonObject.addProperty("sns_id",data.resbody?.id)
//                jsonObject.addProperty("sns_provider",data.resbody.toString())
//                su1002(jsonObject)
//            }
//            override fun onFailure(code: Int) {
//             LogUtil.e(code)
//            }
//
//            override fun onError(throwable: Throwable) {
//                }
//
//        })
//
//    }

    fun su1002(jsonObject: JsonObject) {
        signUpRepository.jw1002(jsonObject, object : SignUpRepository.GetResbodyCallback<JSONObject> {
            override fun onSuccess(data: JSONObject) {
                val jw1002Data = gson.fromJson(data.toString(), JW1002::class.java)
                _su1002Data.value = jw1002Data
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
            }

            override fun onError(throwable: Throwable) {
            }

        })
    }

}