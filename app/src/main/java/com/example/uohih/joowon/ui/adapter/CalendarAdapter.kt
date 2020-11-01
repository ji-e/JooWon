package com.example.uohih.joowon.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.uohih.joowon.R
import com.example.uohih.joowon.database.VacationData
import com.example.uohih.joowon.model.CalendarDayInfo
import com.example.uohih.joowon.util.LogUtil
import kotlinx.android.synthetic.main.grid_item_worker_main.view.*
import java.time.LocalDate
import java.util.*
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.widget.TextView
import android.graphics.Paint


/**
 * 캘린더 아답터
 * mContext: Context
 * listCalendarDayInfo: ArrayList<CalendarDayInfo>: 캘린더 날짜 정보 리스트
 * selectedDate: Date: 선택된 날짜
 */
class CalendarAdapter(private val mContext: Context, val layout: Int, private val listCalendarDayInfo: ArrayList<CalendarDayInfo>,
                      selectedDate: ArrayList<LocalDate>?,
                      val isFutureSelect: Boolean, val isSelectedMulti: Boolean, val isSelectedRanges: Boolean) : BaseAdapter() {

    private var vacationList = arrayListOf<VacationData>()

    private var selectedDateArray = arrayListOf<LocalDate>()

//    private var isFutureSelect = false
//    private var isSelectedDateArray = false
//    private var isSelectedRanges = false

    private var firstPosition = -1
    private var lastPosition = -1
    private var currentPosition = -1

    var selectedDateClickListener: SelectedDateClickListener? = null

    interface SelectedDateClickListener {
        fun onSelectedDateClick(date: ArrayList<LocalDate>)
    }

    override fun getCount(): Int {
        return listCalendarDayInfo.size
    }

    override fun getItem(position: Int): Any? {
        return if (position >= count) null else listCalendarDayInfo[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    init {
        selectedDate?.let { selectedDateArray = selectedDate }
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val day = listCalendarDayInfo[position]
        var convertView = view

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layout, parent, false)
        }

        val cell = convertView?.calendar_cell
        val today = convertView?.calendar_today

        val halfV = convertView?.calendar_half_v
        val allV = convertView?.calendar_all_v


        today?.visibility = View.GONE

        cell?.text = day.getDay()
        val now = (LocalDate.now().toString().replace("-", "")).toInt()
        val selected = (day.getDate().toString().replace("-", "")).toInt()
        if (isFutureSelect || now >= selected) {
            if (isSelectedRanges) {
                if (position in firstPosition..lastPosition || position in lastPosition..firstPosition) {
                    if (firstPosition != -1 && lastPosition != -1) {
                        today?.visibility = View.VISIBLE
                    }
                } else {
                    today?.visibility = View.INVISIBLE
                }
            } else {
                cell?.setOnClickListener {

                    day.getDate()?.let {

                        if (isSelectedMulti) {
                            if (selectedDateArray.contains(it)) {
                                selectedDateArray.remove(it)
                            } else {
                                selectedDateArray.add(it)
                            }
                        } else {
                            selectedDateArray.clear()
                            selectedDateArray.add(it)
                        }

                    }
                    selectedDateClickListener?.onSelectedDateClick(selectedDateArray)
                    notifyDataSetChanged()
                }
            }
        }

        if (firstPosition == -1 && lastPosition == -1) {
            today?.visibility = if (day.isSameDay(selectedDateArray)) View.VISIBLE else View.INVISIBLE
        }
        halfV?.visibility = View.INVISIBLE
        allV?.visibility = View.INVISIBLE

        for (i in vacationList.indices) {
            if (((vacationList[i].date).toString()) == (day.getDate()).toString().replace("-", "")) {
                if ((vacationList[i].use).toString() == "0.5") {
                    halfV?.visibility = View.VISIBLE
                    allV?.visibility = View.INVISIBLE
                } else {
                    halfV?.visibility = View.GONE
                    allV?.visibility = View.VISIBLE
                }
            }
        }

        if (day.isSameDay(LocalDate.now())) {
            cell?.paintFlags = UNDERLINE_TEXT_FLAG
//            cell?.setTextColor(mContext.getColor(R.color.c_cee59a))
            currentPosition = position
        } else {
            if (day.isInMonth()) {
                when {
                    position % 7 + 1 == Calendar.SUNDAY -> cell?.setTextColor(Color.RED)
                    position % 7 + 1 == Calendar.SATURDAY -> cell?.setTextColor(Color.BLUE)
                    else -> cell?.setTextColor(Color.BLACK)
                }
            } else {
                cell?.setTextColor(Color.GRAY)
            }
        }

        convertView?.tag = day

        return convertView!!

    }


    fun setSelectedDate(selectedDateArray: ArrayList<LocalDate>) {
        this.selectedDateArray = selectedDateArray
        notifyDataSetChanged()
    }

    fun setRangesPosition(firstPosition: Int, lastPosition: Int) {
        this.firstPosition = firstPosition
        this.lastPosition = lastPosition
        notifyDataSetChanged()
    }

    fun getCurrentDatePosition():Int {
        return currentPosition
    }
}