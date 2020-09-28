package com.example.uohih.joowon.repository.signin

import com.google.gson.JsonObject
import org.json.JSONObject

class SignInRepository(val dataSource: SignInDataSource) {
    /**
     * 네이버로그인 정보 가져오기
     */
    fun jw1003(accessToken: String, callback: GetResbodyCallback<JSONObject>) {
        dataSource.requestNaverService(accessToken, callback)
    }

    /**
     * 관리자 정보 가져오기
     */
    fun jw1006(jsonObject: JsonObject, callback: GetResbodyCallback<JSONObject>) {
        dataSource.requestSignInService(jsonObject, callback)
    }
//
//    /**
//     * sns_id 등록체크
//     */
//    fun jw1004(jsonObject: JsonObject, callback:GetResbodyCallback<JW1004>){
//        dataSource.requestJW1004( jsonObject, callback)
//    }
//
//    fun jw1006(jsonObject: JsonObject, callback: GetResbodyCallback<JW1006>){
//        dataSource.requestJW1006( jsonObject, callback)
//    }

    interface GetResbodyCallback<json : JSONObject> {
        fun onSuccess(data: json)
        fun onFailure(code: Int)
        fun onError(throwable: Throwable)
    }
}