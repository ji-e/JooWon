package com.example.uohih.joowon.repository

import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.retrofit.GetResbodyCallback
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
import java.net.CookieManager
import java.net.CookiePolicy
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
        /**
         * OkHttpClient 객체 생성 과정
         *
         * 1. OkHttpClient 객체 생성
         * 2. 세션 데이터의 획득을 위해 response 데이터 중 헤더 영역의 쿠키 값을 가로채기 위한 RecivedCookiesInterceptor 추가
         * 3. 서버로 데이터를 보내기 전 세션 데이터 삽입을 위해 AddCookiesInterceptor 추가
         * 4. OkHttpClient 빌드
         *
         * 주의) 가로채기 위한 메소드는 addInterceptor이고 삽입하기 위한 메소드는 addNetworkInterceptor
         */
        var logging: HttpLoggingInterceptor? = HttpLoggingInterceptor()
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//        if (BuildConfig.DEBUGMODE) {
        logging!!.level = HttpLoggingInterceptor.Level.BODY
//        }
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                            .header("User-Agent", "RobClient/1.0")
                            .header("Content-Type", "application/json ; charset=utf8")
                            .header("dataTye", "json")
                            .header("charset", "UTF-8")
                            .header("Accept-Language", "ko-KR")
//                            .header("X-MPZ-DEVICE",framwork.http.core.RetrofitAdapter.deviceUUID)
                            .header("Accept-Encoding", "gzip, deflate")
                            .header("Connection", "Keep-Alive")
                            .header("OS", "android")
                            .method(original.method(), original.body())
                            .build()
                    chain.proceed(request)
                }
                .addInterceptor(logging)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(RetrofitRecivedInterceptor())
                .addNetworkInterceptor(RetrofitAddInterceptor())
//                .cookieJar(WebviewCookieHandler())
                .build()
        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }


}