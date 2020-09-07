package com.example.uohih.joowon.base

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.uohih.joowon.view.CalendarDayInfo
import com.example.uohih.joowon.view.CustomLoadingBar
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.*
import kotlin.collections.ArrayList


open class JWBaseActivity : AppCompatActivity() {

    val mContext: Context by lazy { applicationContext }


    /**
     * 앱 버전 정보 가져오기
     */
    fun getVersionInfo(): String {
        val info: PackageInfo? = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
        return info?.versionName.toString()
    }

    /**
     * 키패드 데이터 세팅
     */
    fun setKeyPadData(): ArrayList<String> {
        val arrayList = ArrayList<String>()
        val randomList = ArrayList<String>()

        for (i in 0 until 12) {
            when (i) {
                9 -> arrayList.add("왼")
                10 -> arrayList.add("0")
                11 -> arrayList.add("오")
                else -> arrayList.add((i + 1).toString())
            }
        }

        arrayList.shuffle()
        var tempIndex = 0
        var index10 = ""
        for (i in 0 until 12) {

            val temp = arrayList[i]
            if (temp != "왼" && temp != "오") {
                if (tempIndex == 9) {
                    tempIndex++
                } else {
                    randomList.add(tempIndex++, temp)
                }
                index10 = temp
            }

        }
        randomList.add(9, "왼")
        randomList.add(10, index10)
        randomList.add(11, "오")

        return randomList
    }

    /**
     * 프리퍼런스에 값 저장
     */
    fun setPreference(key: String, str: String) {
        val pref = getSharedPreferences(key, MODE_PRIVATE)
        val editor = pref.edit().apply { putString(key, str) }
        editor.apply()
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
        val jsonCalendar = JSONObject()
        val instance = if (date != null) LocalDate.parse(date) else LocalDate.now()

        val year = instance.year.toString()                                             //현재 년도
        val month = String.format("%02d", (instance.monthValue))                        //현재 월
        val date = String.format("%02d", instance.dayOfMonth)                           //현재 날짜
        val day = instance.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)    //현재 요일

        jsonCalendar.put("year", year)
        jsonCalendar.put("month", month)
        jsonCalendar.put("date", date)
        jsonCalendar.put("day", day)
        jsonCalendar.put("yyyymmdd", "$year$month$date")

        return jsonCalendar
    }

    /**
     * 사진 각도
     * exifOrientation: Int
     * return Int
     */
    fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    /**
     * 각도 회전
     * bitmap: Bitmap
     * degree: Float
     * return Bitemap
     */
    fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    /**
     * 캘린더 가져오기
     */

    fun getCalendar(currentDate: LocalDate): java.util.ArrayList<CalendarDayInfo> {
        var day: CalendarDayInfo
        val arrayListDayInfo = java.util.ArrayList<CalendarDayInfo>()
        var calendar = currentDate.withDayOfMonth(1)    //1일로 변경
        var dayOfWeek = calendar.dayOfWeek.get(ChronoField.DAY_OF_WEEK) //1일의 요일 구하기
        val thisMonthLastDay = calendar.withDayOfMonth(calendar.month.length(calendar.isLeapYear)).get(ChronoField.DAY_OF_MONTH)

        //현재 달의 1일이 무슨 요일인지 검사
        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek += 7
        }

        //현재 달력화면에서 보이는 지난달의 시작일
        calendar = calendar.minusDays((dayOfWeek).toLong())
        for (i in 0 until dayOfWeek) {
            day = CalendarDayInfo()
            day.setDate(calendar)
            day.setInMonth(false)
            arrayListDayInfo.add(day)

            calendar = calendar.plusDays(1)

        }

        for (i in 1..thisMonthLastDay) {
            day = CalendarDayInfo()
            day.setDate(calendar)
            day.setInMonth(true)
            arrayListDayInfo.add(day)

            calendar = calendar.plusDays(1)
        }

        for (i in 1 until 42 - (thisMonthLastDay + dayOfWeek) + 1) {
            day = CalendarDayInfo()
            day.setDate(calendar)
            day.setInMonth(false)
            arrayListDayInfo.add(day)

            calendar = calendar.plusDays(1)
        }

        return arrayListDayInfo

    }

    /**
     * show loading
     */
    fun showLoading() {
        CustomLoadingBar.showLoadingBar(this)
    }

    /**
     * hide loading
     */
    fun hideLoading() {
        try {
            LogUtil.e("<<<<<<<hideLoading>>>>>>")
            CustomLoadingBar.hideLoadingBar()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
