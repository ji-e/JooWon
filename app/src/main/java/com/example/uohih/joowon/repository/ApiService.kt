package com.example.uohih.joowon.repository

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * api
 *
 * date: 2020-09-24
 * author: jieun
 */
interface ApiService {
    @POST("sign_p/{method}")
    fun signInProcessService(@Path("method") method: String, @Body jsonBodyString: JsonObject): Call<ResponseBody?>

    @POST("base")
    fun BaseProcessService(@Body jsonBodyString: JsonObject?): Call<ResponseBody?>

    @POST("dairyprocess")
    fun EmployeeProcessService(@Body jsonBodyString: JsonObject?): Call<ResponseBody?>

    @POST("postprocess")
    fun PostProcessService(@Body jsonBodyString: JsonObject?): Call<ResponseBody?>

    @POST("boardprocess")
    fun BoardProcessService(@Body jsonBodyString: JsonObject?): Call<ResponseBody?>

    @POST
    fun NaverOpenApiService(@Url url: String?, @Header("Authorization") accT: String?): Call<ResponseBody?>

    companion object {
        const val Base_URL = "https://joowon12.herokuapp.com/process/"
//        const val Base_URL = "http://10.0.2.2:8080/process/"
        const val BASE_URL_NAVER_API = "https://openapi.naver.com/v1/nid/me"
    }
}