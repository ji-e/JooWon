package com.example.uohih.joowon.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class RetrofitAddInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed(Runnable {
//            LogUtil.e(JWBaseActivity().getHashSet("cookie", HashSet()))
//        },0)
        RetrofitSharedPreferences().getdd()

//        val jw = JWBaseApplication()
//        for (cookie in jw.getHashSet(JWBaseApplication.KEY_COOKIE, HashSet<String>())) {
//            builder.addHeader("Cookie", cookie)
//            LogUtil.v("OkHttp", "Adding Header: $cookie") // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
//        }

        return chain.proceed(builder.build())
    }

}