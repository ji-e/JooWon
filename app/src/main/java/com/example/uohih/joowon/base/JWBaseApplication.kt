package com.example.uohih.dailylog.base

import android.app.Application


class JWBaseApplication : Application() {


    /**
     * ------------- 캘린더 팝업 날짜 세팅 -------------
     */
    fun setSeleteDate(selecteDate: String) {
        Companion.selecteDate = selecteDate
    }

    fun getSeleteDate(): String {
        return selecteDate
    }


    companion object {
        private var selecteDate = JWBaseActivity().getToday().get("yyyymmdd").toString()
    }

}