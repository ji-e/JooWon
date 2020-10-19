package com.example.uohih.joowon.repository

import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.retrofit.GetResbodyCallback

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import java.util.*

open class JWBaseRepository {
    /**
     * 네이버Api
     */
    fun requestNaverService(accessToken: String, callback: GetResbodyCallback) {
        val retroClient = JWBaseDataSource.instance.createBaseApi()
        val apiService = JWBaseDataSource.apiService

        retroClient.requestDataRetrofit(apiService.naverOpenApiService(ApiService.BASE_URL_NAVER_API, "Bearer $accessToken"), object : GetResbodyCallback {

            override fun onFailure(code: Int) {
                callback.onFailure(code)
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }


            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                LogUtil.d(resbodyData.toString())
                callback.onSuccess(code, resbodyData)

            }
        })
    }


    fun requestSignInService(jsonObject: JsonObject, callback: GetResbodyCallback) {
        val retroClient = JWBaseDataSource.instance.createBaseApi()
        val apiService = JWBaseDataSource.apiService

        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", UUID.randomUUID().toString().replace("-", ""))

        LogUtil.d("requsetBody:  ", jsonObject)

        retroClient.requestDataRetrofit(apiService.signInProcessService(Constants.SERVICE_ADMIN, jsonObject), object : GetResbodyCallback {

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
                callback.onSuccess(code, resbodyData)
            }
        })
    }

    fun requestBaseService(jsonObject: JsonObject, callback: GetResbodyCallback) {
        val retroClient = JWBaseDataSource.instance.createBaseApi()
        val apiService = JWBaseDataSource.apiService

        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", UUID.randomUUID().toString().replace("-", ""))

        LogUtil.d("requsetBody:  ", jsonObject)

        retroClient.requestDataRetrofit(apiService.baseProcessService(Constants.SERVICE_EMPLOYEE, jsonObject), object : GetResbodyCallback {

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
                callback.onSuccess(code, resbodyData)
            }
        })
    }

    fun requestBaseUploadService(part: MultipartBody.Part,  jsonObject: JsonObject, callback: GetResbodyCallback) {
        val retroClient = JWBaseDataSource.instance.createBaseApi()
        val apiService = JWBaseDataSource.apiService

        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", UUID.randomUUID().toString().replace("-", ""))

        LogUtil.d(part.body(), "requsetBody:  ", jsonObject)

        retroClient.requestDataRetrofit(apiService.baseUploadProcessService(part, jsonObject), object : GetResbodyCallback {

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
                callback.onSuccess(code, resbodyData)
            }
        })
    }

}