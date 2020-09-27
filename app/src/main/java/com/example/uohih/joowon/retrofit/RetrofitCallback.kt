package com.example.uohih.joowon.retrofit

import com.example.uohih.joowon.model.Result
import org.json.JSONObject


/**
 * RetrofitCallback
 *
 * date: 2020-09-26
 * author: jieun
 */
internal interface RetroCallback {
    fun onError(t: Throwable)
    fun onSuccess(code: Int, resbodyData: JSONObject)
    fun onFailure(code: Int)
}
