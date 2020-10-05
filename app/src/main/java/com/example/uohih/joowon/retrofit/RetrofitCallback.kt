package com.example.uohih.joowon.retrofit

import org.json.JSONObject


/**
 * RetrofitCallback
 *
 * date: 2020-09-26
 * author: jieun
 */
interface RetroCallback {
    fun onError(t: Throwable)
    fun onSuccess(code: Int, resbodyData: JSONObject)
    fun onFailure(code: Int)
}
