package com.example.uohih.joowon.data.signup

import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.api.ApiService
import com.example.uohih.joowon.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

class SignUpDataSourceImpl(private val apiService: ApiService) : SignUpDataSource {

    override fun jw1002(jsonObject: JsonObject, success: (JW1002) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.signInProcessService(Constants.SERVICE_ADMIN, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW1002::class.java)
                        success(response)
                    } else {
                        failure(HttpException(this))
                    }
                }
            }
        })
    }

    override fun jw2001(jsonObject: JsonObject, success: (JW2001) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.signInProcessService(Constants.SERVICE_ADMIN, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW2001::class.java)
                        success(response)
                    } else {
                        failure(HttpException(this))
                    }
                }
            }
        })
    }


    private fun setJsonObject(jsonObject: JsonObject): JsonObject {
        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", UUID.randomUUID().toString().replace("-", ""))

        return jsonObject
    }
}