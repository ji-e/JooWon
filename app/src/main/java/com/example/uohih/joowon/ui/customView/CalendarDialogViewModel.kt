package com.example.uohih.joowon.ui.customView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.model.CalendarDayInfo
import java.time.LocalDate

class CalendarDialogViewModel : ViewModel() {
    val liveCalendarList = MutableLiveData<ArrayList<ArrayList<CalendarDayInfo>>>()

    fun setCalendarList(date: LocalDate) {
        val month = ArrayList<ArrayList<CalendarDayInfo>>()

        for (i in 0 until 100) {
            val d = date.minusMonths((100 - i).toLong())
            val calendarInfo = JWBaseActivity().getCalendar(d)
            month.add(calendarInfo)
        }

        month.add(JWBaseActivity().getCalendar(date))

        for (i in 1 until 101) {
            val d = date.plusMonths(i.toLong())
            val calendarInfo = JWBaseActivity().getCalendar(d)
            month.add(calendarInfo)
        }
        liveCalendarList.postValue(month)
    }

}