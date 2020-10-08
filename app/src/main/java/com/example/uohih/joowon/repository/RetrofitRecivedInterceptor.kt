package com.example.uohih.joowon.repository

import android.content.Context
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException


class RetrofitRecivedInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
//        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
//            val cookies: HashSet<String> = HashSet()
//            for (header in originalResponse.headers("Set-Cookie")) {
//                cookies.add(header)
//            }
//            JWBaseApplication().putHashSet("cookie", cookies)
//        }
        return originalResponse
    }

}