package com.example.uohih.joowon.base

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.uohih.joowon.view.CalendarDayInfo
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


open class JWBaseActivity : AppCompatActivity() {

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
        var date = String.format("%02d", instance.get(Calendar.DAY_OF_MONTH))
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
    fun getCalendar(dateForCurrentMonth: Date): java.util.ArrayList<CalendarDayInfo> {
        var dayOfWeek: Int
        val thisMonthLastDay: Int
        val arrayListDayInfo = java.util.ArrayList<CalendarDayInfo>()

        val calendar = Calendar.getInstance()
        calendar.time = dateForCurrentMonth

        calendar.set(Calendar.DATE, 1)//1일로 변경
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)//1일의 요일 구하기
        LogUtil.d("dayOfWeek = $dayOfWeek")

        if (dayOfWeek == Calendar.SUNDAY) { //현재 달의 1일이 무슨 요일인지 검사
            dayOfWeek += 7
        }

        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)


        var day: CalendarDayInfo

        calendar.add(Calendar.DATE, -1 * (dayOfWeek - 1)) //현재 달력화면에서 보이는 지난달의 시작일
        for (i in 0 until dayOfWeek - 1) {
            day = CalendarDayInfo()
            day.setDate(calendar.time)
            day.setInMonth(false)
            arrayListDayInfo.add(day)

            calendar.add(Calendar.DATE, +1)
        }

        for (i in 1..thisMonthLastDay) {
            day = CalendarDayInfo()
            day.setDate(calendar.time)
            day.setInMonth(true)
            arrayListDayInfo.add(day)

            calendar.add(Calendar.DATE, +1)
        }

        for (i in 1 until 42 - (thisMonthLastDay + dayOfWeek - 1) + 1) {
            day = CalendarDayInfo()
            day.setDate(calendar.time)
            day.setInMonth(false)
            arrayListDayInfo.add(day)

            calendar.add(Calendar.DATE, +1)
        }

        return arrayListDayInfo

    }


}
