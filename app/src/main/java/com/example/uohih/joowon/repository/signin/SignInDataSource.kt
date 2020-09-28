package com.example.uohih.joowon.repository.signin

import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.JW1003
import com.example.uohih.joowon.model.JW1004
import com.example.uohih.joowon.model.JW1006
import com.example.uohih.joowon.retrofit.ApiService.BASE_URL_NAVER_API
import com.example.uohih.joowon.retrofit.RetroCallback
import com.example.uohih.joowon.retrofit.RetroClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.UUID.randomUUID

class SignInDataSource {

    /**
     * 네이버Api
     */
    fun requestNaverService(accessToken: String, callback: SignInRepository.GetResbodyCallback<JSONObject>) {
        val retroClient = RetroClient.getInstance().createBaseApi()
        val apiService = RetroClient.apiService

        retroClient.requestDataRetrofit(apiService.NaverOpenApiService(BASE_URL_NAVER_API, "Bearer $accessToken"), object : RetroCallback {

            override fun onFailure(code: Int) {
                callback.onFailure(code)
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }


            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                LogUtil.d(resbodyData.toString())
                callback.onSuccess(resbodyData)

            }
        })
    }


    fun requestSignInService(jsonObject: JsonObject, callback: SignInRepository.GetResbodyCallback<JSONObject>) {
        val retroClient = RetroClient.getInstance().createBaseApi()
        val apiService = RetroClient.apiService

        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", randomUUID().toString().replace("-", ""))

        LogUtil.d("requsetBody:  ", jsonObject)

        retroClient.requestDataRetrofit(apiService.LoginProcessService(Constants.SERVICE_ADMIN, jsonObject), object : RetroCallback {

            override fun onFailure(code: Int) {
                callback.onFailure(code)
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }

            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                LogUtil.d(resbodyData.toString())
                callback.onSuccess(resbodyData)
            }
        })
    }
}