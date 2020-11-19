package com.example.uohih.joowon.api

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Multipart



/**
 * api
 *
 * date: 2020-09-24
 * author: jieun
 */
interface ApiService {
    @POST("sign_p/{method}")
    fun signInProcessService(@Path("method") method: String, @Body jsonBodyString: JsonObject): Call<ResponseBody?>

    @POST("base/{method}")
    fun baseProcessService(@Path("method") method: String, @Body jsonBodyString: JsonObject?): Call<ResponseBody?>

    @Multipart
    @POST("base/employee/upload")
    fun baseUploadProcessService( @Part file: MultipartBody.Part, @Part("data") jsonBodyString: JsonObject?): Call<ResponseBody?>

    @POST
    fun naverOpenApiService(@Url url: String?, @Header("Authorization") accT: String?): Call<ResponseBody?>

    companion object {
        const val Base_URL = "https://joowon12.herokuapp.com/process/"
        const val BASE_URL_NAVER_API = "https://openapi.naver.com/v1/nid/me"
    }
}