package com.example.uohih.joowon.repository

import com.google.gson.JsonObject
import org.json.JSONObject

open class JWBaseRepository(val dataSource: JWBaseDataSource) {

    fun requestNaverService(accessToken: String, callback: GetResbodyCallback<JSONObject>) {
        dataSource.requestNaverService(accessToken, callback)
    }

    fun requestSignInService(reqbody: JsonObject, callback: GetResbodyCallback<JSONObject>) {
        dataSource.requestSignInService(reqbody, callback)
    }

    interface GetResbodyCallback<json : JSONObject> {
        fun onSuccess(data: json)
        fun onFailure(code: Int)
        fun onError(throwable: Throwable)
    }
}