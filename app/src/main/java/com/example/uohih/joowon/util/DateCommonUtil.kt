package com.example.uohih.joowon.util

import com.example.uohih.joowon.Constants
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class DateCommonUtil {
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

    fun setFormatHpDate(date: String): String {
        return try {
            if (date.length == 8) {
                (Constants.YYYYMMDD_PATTERN).toRegex().replace(date, "$1-$2-$3")
            } else ""
        } catch (e: Exception) {
            ""
        }
    }

    fun setFormatDate(date: String): String {
        return try {
            date.replace("-", "")
        } catch (e: Exception) {
            ""
        }
    }
}