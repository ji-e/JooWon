package com.example.uohih.joowon.model

import java.time.LocalDate

class CalendarDayInfo {
    private var inMonth: Boolean = false
    private var date: LocalDate? = null


    fun getDay(): String {
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


    fun isSameDay(selectedDate: ArrayList<LocalDate>): Boolean {
        if (date == null) {
            return false
        } else {
            for (i in 0 until selectedDate.size) {
                if (selectedDate[i].isEqual(date)) return true
            }
            return false
        }
    }

    fun isSameDay(selectedDate: LocalDate): Boolean {
        return if (date == null) false else selectedDate.isEqual(date)
    }
}


