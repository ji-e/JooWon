package com.example.uohih.joowon.base

import android.app.Application
import com.example.uohih.joowon.util.DateCommonUtil

import com.example.uohih.joowon.util.UseSharedPreferences
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.collections.ArrayList


class JWBaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        useSharedPreference = UseSharedPreferences(this)

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