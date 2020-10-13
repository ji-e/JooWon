package com.example.uohih.joowon.repository

import com.example.uohih.joowon.BuildConfig
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class JWBaseDataSource private constructor() {
    private object SingletonHolder {
        internal val INSTANCE = JWBaseDataSource()
    }

    fun createBaseApi(): JWBaseDataSource {
        apiService = create(ApiService::class.java)
        return this
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the `service` interface.
     */
    fun <T> create(service: Class<T>?): T {
        if (service == null) {
            throw RuntimeException("Api service is null!")
        }
        return retrofit.create(service)
    }

    fun requestDataRetrofit(apiCall: Call<ResponseBody?>, callback: GetResbodyCallback) {
        apiCall.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {
                    var response_body = ""
                    if (response.isSuccessful) {
                        response_body = response.body()!!.string()

                        val jsonArray = JSONArray()
                        val jsonObject = JSONObject(response_body)
                        jsonArray.put(jsonObject)

                        if (response.body() != null) {
                            callback.onSuccess(response.code(), jsonObject)
                        }

                    } else {
                        if (response.body() != null) {
                            callback.onFailure(response.code())
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    companion object {
        lateinit var apiService: ApiService
        private var baseUrl = ApiService.Base_URL
        private lateinit var retrofit: Retrofit
        val instance: JWBaseDataSource
            get() = SingletonHolder.INSTANCE
    }

    init {
        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient().newBuilder()
//                .addInterceptor(logging)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(JavaNetCookieJar(JWBaseApplication.cookieManager))
//                .cookieJar(WebviewCookieHandler())
                .build()

        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }

}