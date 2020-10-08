//package com.example.uohih.joowon.retrofit
//
//import android.util.Log
//import com.example.uohih.joowon.base.LogUtil
//import okhttp3.ResponseBody
//import org.json.JSONArray
//import org.json.JSONException
//import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.io.IOException
//
///**
// * Retrofit 클라이언트
// *
// *
// * date: 2020-09-24
// * author: jieun
// */
//class RetroClient private constructor() {
//    private object SingletonHolder {
//        internal val INSTANCE = RetroClient()
//    }
//
//    fun createBaseApi(): RetroClient {
//        apiService = create(ApiService::class.java)
//        return this
//    }
//
//    /**
//     * create you ApiService
//     * Create an implementation of the API endpoints defined by the `service` interface.
//     */
//    fun <T> create(service: Class<T>?): T {
//        if (service == null) {
//            throw RuntimeException("Api service is null!")
//        }
//        return retrofit.create(service)
//    }
//
//    fun requestDataRetrofit(apiCall: Call<ResponseBody?>, callback: RetroCallback) {
//        apiCall.enqueue(object : Callback<ResponseBody?> {
//            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
//                try {
//                    var response_body = ""
//                    if (response.isSuccessful) {
//                        response_body = response.body()!!.string()
//
//                        val jsonArray = JSONArray()
//                        val jsonObject = JSONObject(response_body)
//                        jsonArray.put(jsonObject)
//
//                        if (response.body() != null) {
//                            callback.onSuccess(response.code(), jsonObject)
//                        }
//
//                    } else {
//                        if (response.body() != null) {
//                            callback.onFailure(response.code())
//                        }
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
//                callback.onError(t)
//            }
//        })
//    }
//
//    companion object {
//        lateinit var apiService: ApiService
//        private var baseUrl = ApiService.Base_URL
//        private lateinit var retrofit: Retrofit
//        val instance: RetroClient
//            get() = SingletonHolder.INSTANCE
//    }
//
//    init {
//        retrofit = Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(baseUrl)
//                .build()
//    }
//}