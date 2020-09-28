package com.example.uohih.joowon.repository.signup

import com.google.gson.JsonObject
import org.json.JSONObject

class SignUpRepository(val dataSource: SignUpDataSource) {

    fun jw1001(reqbody: JsonObject, callback: GetResbodyCallback<JSONObject>) {
        dataSource.requestSignUpService(reqbody, callback)
    }

    fun jw1002(reqbody: JsonObject, callback: GetResbodyCallback<JSONObject>) {
        dataSource.requestSignUpService(reqbody, callback)
    }

//    fun su1003(accessToken: String, callback:GetResbodyCallback<JW1003>){
//        dataSource.requestSU1003( accessToken, callback)
//    }

    interface GetResbodyCallback<json : JSONObject> {
        fun onSuccess(data: json)
        fun onFailure(code: Int)
        fun onError(throwable:Throwable)
    }
}

