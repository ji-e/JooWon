package com.example.uohih.joowon.repository.signup

import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.signup.SU1001
import com.example.uohih.joowon.model.signup.SU1002
import com.example.uohih.joowon.model.signup.SU1003
import com.example.uohih.joowon.retrofit.RetroCallback
import com.example.uohih.joowon.retrofit.RetroClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.poi.ss.formula.functions.T
import org.json.JSONObject
import java.util.*
import java.util.UUID.randomUUID


class SignUpDataSource {

    fun requestSU1001(jsonObject: JsonObject, callback: SignUpRepository.GetResbodyCallback<SU1001>) {
        val retroClient = RetroClient.getInstance().createBaseApi()
        val apiService = RetroClient.apiService


        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", randomUUID().toString().replace("-",""))

        LogUtil.d("requsetBody:  ", jsonObject)

        retroClient.requestDataRetrofit(apiService.LoginProcessService(Constants.SERVICE_ADMIN, jsonObject), object : RetroCallback {
            override fun onFailure(code: Int) {
                callback.onFailure(code)
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }

            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                val gson = Gson()
                LogUtil.d(resbodyData.toString())
                callback.onSuccess(gson.fromJson(resbodyData.toString(), SU1001::class.java))

            }
        })
    }

    fun requestSU1002(jsonObject: JsonObject, callback: SignUpRepository.GetResbodyCallback<SU1002>) {
        val retroClient = RetroClient.getInstance().createBaseApi()
        val apiService = RetroClient.apiService

        retroClient.requestDataRetrofit(apiService.LoginProcessService(Constants.SERVICE_ADMIN, jsonObject), object : RetroCallback {

            override fun onFailure(code: Int) {
                callback.onFailure(code)
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }


            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                val gson = Gson()
                LogUtil.d(resbodyData.toString())
                callback.onSuccess(gson.fromJson(resbodyData.toString(), SU1002::class.java))

            }
        })
    }

    fun requestSU1003(accessToken:String, callback: SignUpRepository.GetResbodyCallback<SU1003>) {
        val retroClient = RetroClient.getInstance2().createBaseApi()
        val apiService = RetroClient.apiService

        retroClient.requestDataRetrofit(apiService.NaverOpenApiService("Bearer $accessToken"), object : RetroCallback {

            override fun onFailure(code: Int) {
                callback.onFailure(code)
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }


            override fun onSuccess(code: Int, resbodyData: JSONObject) {
                val gson = Gson()
                LogUtil.d(resbodyData.toString())
                callback.onSuccess(gson.fromJson(resbodyData.toString(), SU1003::class.java))

            }
        })
    }
}
