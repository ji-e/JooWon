package com.example.uohih.joowon.base

import android.app.Application
import com.example.uohih.joowon.BuildConfig
import com.example.uohih.joowon.module.apiModule
import com.example.uohih.joowon.module.dataSourceModule
import com.example.uohih.joowon.module.repositoryModule
import com.example.uohih.joowon.module.viewModelModule
import com.example.uohih.joowon.util.UseSharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import java.net.CookieManager
import java.net.CookiePolicy


class JWBaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        useSharedPreference = UseSharedPreferences(this)

        startKoin {
            androidContext(this@JWBaseApplication)
            modules(listOf(
                    apiModule,
                    repositoryModule,
                    dataSourceModule,
                    viewModelModule))
            logger(if (BuildConfig.DEBUG) {
                AndroidLogger()
            } else {
                EmptyLogger()
            })
        }

    }



    companion object {
        lateinit var useSharedPreference: UseSharedPreferences

        val cookieManager = CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }

//        val cookieJar by lazy {
//            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(UseSharedPreferences.preferences))
//        }

    }

}