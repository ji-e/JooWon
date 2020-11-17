package com.example.uohih.joowon.data.vacation

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

class VacationDataSourceImpl(private val apiService: ApiService) : VacationDataSource {

    override fun jw4001(jsonObject: JsonObject, success: (JW4001) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.baseProcessService(Constants.SERVICE_VACATION, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW4001::class.java)
                        success(response)
                    } else {
                        failure(HttpException(this))
                    }
                }
            }
        })
    }

    override fun jw4004(jsonObject: JsonObject, success: (JW4004) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.baseProcessService(Constants.SERVICE_VACATION, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW4004::class.java)
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