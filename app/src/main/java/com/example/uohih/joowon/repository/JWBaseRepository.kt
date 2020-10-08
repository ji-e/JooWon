package com.example.uohih.joowon.repository

import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.retrofit.GetResbodyCallback

import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.*

open class JWBaseRepository {

//    fun requestNaverService(accessToken: String, callback: GetResbodyCallback<JSONObject>) {
//        dataSource.requestNaverService(accessToken, callback)
//    }
//
//    fun requestSignInService(reqbody: JsonObject, callback: GetResbodyCallback<JSONObject>) {
//        dataSource.requestSignInService(reqbody, callback)
//    }
//
//    interface GetResbodyCallback<json : JSONObject> {
//        fun onSuccess(data: json)
//        fun onFailure(code: Int)
//        fun onError(throwable: Throwable)
//    }

    /**
     * 네이버Api
     */
    fun requestNaverService(accessToken: String, callback: GetResbodyCallback) {
        val retroClient = JWBaseDataSource.instance.createBaseApi()
        val apiService = JWBaseDataSource.apiService

        retroClient.requestDataRetrofit(apiService.NaverOpenApiService(ApiService.BASE_URL_NAVER_API, "Bearer $accessToken"), object : GetResbodyCallback {

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

}