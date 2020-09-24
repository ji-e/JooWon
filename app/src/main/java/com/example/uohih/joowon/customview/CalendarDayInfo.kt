package com.example.uohih.joowon.customview

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CalendarDayInfo {
    private var inMonth: Boolean = false
    private var date: LocalDate? = null



    fun getDay(): String {
        val sdf = SimpleDateFormat("d", Locale.getDefault())
//        return sdf.format(date)
        return date?.dayOfMonth.toString()
    }

    fun isInMonth(): Boolean {
        return inMonth
    }

    fun setInMonth(inMonth: Boolean) {
        this.inMonth = inMonth
    }

    fun getDate(): LocalDate? {
        return date
    }

    fun setDate(date: LocalDate) {
        this.date = date
    }


    fun isSameDay(date1: LocalDate): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
//        cal1. = date1
//        cal2.time = this.date

        return if (date == null) false else date1.isEqual(date)

//        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}


