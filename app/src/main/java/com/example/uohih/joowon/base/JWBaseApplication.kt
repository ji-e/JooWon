package com.example.uohih.joowon.base

import android.app.Application
import com.example.uohih.joowon.BuildConfig
import com.example.uohih.joowon.module.apiModule
import com.example.uohih.joowon.module.dataSourceModule
import com.example.uohih.joowon.module.repositoryModule
import com.example.uohih.joowon.module.viewModelModule
import com.example.uohih.joowon.util.DateCommonUtil
import com.example.uohih.joowon.util.LogUtil

import com.example.uohih.joowon.util.UseSharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.collections.ArrayList


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


    /**
     * ------------- 캘린더 팝업 날짜 세팅 -------------
     */
    fun setSelectDate(selectDate: String) {
        Companion.selectDate = selectDate
    }

    fun getSelectDate(): String {
        return selectDate
    }

    /**
     * ------------- 삭제 항목 세팅 -------------
     */
    fun setDeleteItem(deleteItem: ArrayList<String>?) {
        Companion.deleteItem.clear()
        if (deleteItem != null) {
            LogUtil.d(deleteItem.toString())
            Companion.deleteItem = deleteItem
        }
    }

    fun getDeleteItem(): ArrayList<String> {
        return deleteItem
    }


    companion object {
        private var selectDate = DateCommonUtil().getToday().get("yyyymmdd").toString()
        private var deleteItem = ArrayList<String>()//삭제 항목
        lateinit var useSharedPreference: UseSharedPreferences

        val cookieManager = CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }

//        val cookieJar by lazy {
//            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(UseSharedPreferences.preferences))
//        }

    }

}