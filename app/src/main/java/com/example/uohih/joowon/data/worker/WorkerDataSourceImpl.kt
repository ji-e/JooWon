package com.example.uohih.joowon.data.worker

import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.api.ApiService
import com.example.uohih.joowon.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

class WorkerDataSourceImpl(private val apiService: ApiService) : WorkerDataSource {

    override fun jw3002(part: MultipartBody.Part, jsonObject: JsonObject, success: (JW3002) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.baseUploadProcessService(part, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW3002::class.java)
                        success(response)
                    } else {
                        failure(HttpException(this))
                    }
                }
            }
        })
    }

    override fun jw3003(part: MultipartBody.Part, jsonObject: JsonObject, success: (JW3003) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.baseUploadProcessService(part, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW3003::class.java)
                        success(response)
                    } else {
                        failure(HttpException(this))
                    }
                }
            }
        })
    }

    override fun jw3004( jsonObject: JsonObject, success: (JW3004) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.baseProcessService(Constants.SERVICE_EMPLOYEE, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW3004::class.java)
                        success(response)
                    } else {
                        failure(HttpException(this))
                    }
                }
            }
        })
    }

    override fun jw4003( jsonObject: JsonObject, success: (JW4003) -> Unit, failure: (Throwable) -> Unit) {
        val call = apiService.baseProcessService(Constants.SERVICE_VACATION, setJsonObject(jsonObject))
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                failure(t)
            }

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                with(response) {
                    val body = body()
                    if (isSuccessful && body != null) {
                        val response = Gson().fromJson(body.string(), JW4003::class.java)
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