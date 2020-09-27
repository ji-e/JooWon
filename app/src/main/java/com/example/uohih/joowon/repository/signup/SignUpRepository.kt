package com.example.uohih.joowon.repository.signup

import com.example.uohih.joowon.model.signup.SU1001
import com.example.uohih.joowon.model.signup.SU1002
import com.example.uohih.joowon.model.signup.SU1003
import com.google.gson.JsonObject

class SignUpRepository(val dataSource: SignUpDataSource) {

    fun su1001(reqbody:JsonObject, callback:GetResbodyCallback<SU1001>){
        dataSource.requestSU1001( reqbody, callback)
    }

    fun su1002(reqbody:JsonObject, callback:GetResbodyCallback<SU1002>){
        dataSource.requestSU1002( reqbody, callback)
    }

    fun su1003(accessToken: String, callback:GetResbodyCallback<SU1003>){
        dataSource.requestSU1003( accessToken, callback)
    }

    interface GetResbodyCallback<T>{
        fun onSuccess(data: T)
        fun onFailure(code: Int)
        fun onError(throwable:Throwable)
    }
}

