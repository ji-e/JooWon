package com.example.uohih.joowon.base

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Handler
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


open class JWBaseActivity : Activity() {

    var mContext: Context? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = applicationContext
//        setContentView(R.layout.activity_intro)
    }


    /**
     * 앱 버전 정보 가져오기
     */
    fun getVersionInfo(): String {
        val info: PackageInfo? = mContext?.packageManager?.getPackageInfo(mContext?.packageName, 0)
        return info?.versionName.toString()
    }

    /**
     * 키패드 데이터 세팅
     */
    fun setkeyPadData(): ArrayList<String> {
        val arrayList = ArrayList<String>()
        for (i in 0 until 12) {
            when (i) {
                9 -> arrayList.add("왼")
                10 -> arrayList.add("0")
                11 -> arrayList.add("오")
                else -> arrayList.add((i + 1).toString())
            }
        }
        return arrayList
    }

    /**
     * 프리퍼런스에 값 저장
     */
    fun setPreference(key: String, str: String) {
        val pref = getSharedPreferences(key, MODE_PRIVATE)
        var editor = pref.edit()
        editor.putString(key, str)
        editor.commit()
    }

    /**
     * 프리퍼런스 값 가져오기
     */
    fun getPreference(key: String): String {
        val pref = getSharedPreferences(key, MODE_PRIVATE)
        return pref.getString(key, "")
    }

    /**
     * 앱 종료
     */
    fun exit() {
        finishAffinity()
        Handler().postDelayed({
            android.os.Process.killProcess(android.os.Process.myPid())
        }, 200)
    }

    /**
     * 현재 날짜 구하기
     * return JSONObject
     */
    fun getToday(): JSONObject {
        return getToday(null)
    }

    /**
     * 현재 날짜 구하기
     * date: String?: date값에 따른 JSONObject 생성
     * return JSONObject
     */
    fun getToday(date: String?): JSONObject {
        var jsonCalendar = JSONObject()
        val instance = Calendar.getInstance()
        if (date != null) {
            instance.set(date.substring(0, 4).toInt(), date.substring(4, 6).toInt() - 1, date.substring(6).toInt())
        }

        //현재 년도
        val year = instance.get(Calendar.YEAR).toString()
        //현재 월
        var month = String.format("%02d", (instance.get(Calendar.MONTH) + 1))
        //현재 날짜
        var date = String.format("%02d",instance.get(Calendar.DAY_OF_MONTH))
        //현재 월의 주
        val week = instance.get(Calendar.WEEK_OF_MONTH).toString()
        //현재 요일
        var day = instance.get(Calendar.DAY_OF_WEEK).toString()


//        // 한자리수 앞에 0표기
//        if (month.toInt() < 10) month = "0$month"
//        if (date.toInt() < 10) date = "0$date"

        // 요일로 변환
        when (day) {
            "1" -> day = "일"
            "2" -> day = "월"
            "3" -> day = "화"
            "4" -> day = "수"
            "5" -> day = "목"
            "6" -> day = "금"
            "7" -> day = "토"
        }

        jsonCalendar.put("year", year)
        jsonCalendar.put("month", month)
        jsonCalendar.put("date", date)
        jsonCalendar.put("week", week)
        jsonCalendar.put("day", day)
        jsonCalendar.put("yyyymmdd", "$year$month$date")

        return jsonCalendar
    }



}
