package com.example.uohih.joowon.module

import com.example.uohih.joowon.BuildConfig
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.api.ApiService
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule: Module = module {
    single {
        OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .cookieJar(JavaNetCookieJar(JWBaseApplication.cookieManager))
                .addInterceptor(get<HttpLoggingInterceptor>())
                .build()
    }

//    single {
//        Interceptor { chain: Interceptor.Chain ->
//            val original = chain.request()
//            chain.proceed(original.newBuilder().apply {
//                addHeader("Authorizaion_Header", "access_token")
//            }.build())
//        }
//    }

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single {
        Retrofit.Builder()
                .client(get())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.Base_URL)
                .build()
//                .create(ApiService::class.java)
    }

    single {
        get<Retrofit>().create(
                ApiService::class.java
        )
    }
}
