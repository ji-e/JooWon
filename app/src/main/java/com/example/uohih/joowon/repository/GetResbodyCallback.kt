package com.example.uohih.joowon.retrofit

import org.json.JSONObject


/**
 * GetResbodyCallback
 *
 * date: 2020-09-26
 * author: jieun
 */
interface GetResbodyCallback {
    fun onSuccess(code: Int, resbodyData: JSONObject)
    fun onFailure(code: Int)
    fun onError(throwable: Throwable)
}
