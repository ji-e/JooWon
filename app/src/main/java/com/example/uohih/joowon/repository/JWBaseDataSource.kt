package com.example.uohih.joowon.repository

import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.retrofit.ApiService
import com.example.uohih.joowon.retrofit.RetroCallback
import com.example.uohih.joowon.retrofit.RetroClient
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.*

class JWBaseDataSource {
    /**
     * 네이버Api
     */
    fun requestNaverService(accessToken: String, callback: JWBaseRepository.GetResbodyCallback<JSONObject>) {
        val retroClient = RetroClient.instance.createBaseApi()
        val apiService = RetroClient.apiService

        retroClient.requestDataRetrofit(apiService.NaverOpenApiService(ApiService.BASE_URL_NAVER_API, "Bearer $accessToken"), object : RetroCallback {

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


    fun requestSignInService(jsonObject: JsonObject, callback: JWBaseRepository.GetResbodyCallback<JSONObject>) {
        val retroClient = RetroClient.instance.createBaseApi()
        val apiService = RetroClient.apiService

        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", UUID.randomUUID().toString().replace("-", ""))

        LogUtil.d("requsetBody:  ", jsonObject)

        retroClient.requestDataRetrofit(apiService.signInProcessService(Constants.SERVICE_ADMIN, jsonObject), object : RetroCallback {

            override fun onFailure(code: Int) {
                LogUtil.d(code)
                callback.onFailure(code)
            }

            override fun onError(t: Throwable) {
                LogUtil.d(t.toString())
                callback.onError(t)
            }

            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                LogUtil.d(resbodyData.toString())
                callback.onSuccess(resbodyData)
            }
        })
    }
}