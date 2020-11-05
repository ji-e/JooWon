package com.example.uohih.joowon.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.uohih.joowon.database.VacationData
import com.example.uohih.joowon.model.CalendarDayInfo
import kotlinx.android.synthetic.main.viewpager_worker_main_calendar_cell.view.*
import kotlinx.android.synthetic.main.dialog_calendar_cell.view.*
import java.time.LocalDate
import java.util.*
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.util.AttributeSet
import android.widget.TextView
import com.example.uohih.joowon.R
import com.example.uohih.joowon.model.VacationList
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import com.example.uohih.joowon.util.UICommonUtil
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates


/**
 * 캘린더 아답터
 * mContext: Context
 * listCalendarDayInfo: ArrayList<CalendarDayInfo>: 캘린더 날짜 정보 리스트
 * selectedDate: Date: 선택된 날짜
 */
class CalendarAdapter() : BaseAdapter() {

    private lateinit var mContext: Context
    private lateinit var listCalendarDayInfo: ArrayList<CalendarDayInfo>        // 월간날짜정보
    private lateinit var vacationList: ArrayList<VacationList>                  // 휴가리스트
    private var selectedDateArray = arrayListOf<LocalDate>() // 선택날짜리스트
    private var isSelectedRanges: Boolean = false                               // 범위선택가능여부
    private var layout by Delegates.notNull<Int>()

    private var firstPosition = -1  // 첫번째선택날짜포지션
    private var lastPosition = -1   // 마지막선택날짜포지션
    private var currentPosition = -1// 현재날짜포지션

    var selectedDateClickListener: SelectedDateClickListener? = null

    interface SelectedDateClickListener {
        fun onSelectedDateClick(date: ArrayList<LocalDate>)
    }


    constructor(mContext: Context, layout: Int, listCalendarDayInfo: ArrayList<CalendarDayInfo>,
                selectedDate: ArrayList<LocalDate>?, isSelectedRanges: Boolean) : this() {
        this.mContext = mContext
        this.layout = layout
        this.listCalendarDayInfo = listCalendarDayInfo
        this.isSelectedRanges = isSelectedRanges
        selectedDate?.let { selectedDateArray = selectedDate }
    }

    constructor(mContext: Context, layout: Int, listCalendarDayInfo: ArrayList<CalendarDayInfo>, vacationList: ArrayList<VacationList>) : this() {
        this.mContext = mContext
        this.layout = layout
        this.listCalendarDayInfo = listCalendarDayInfo
        this.vacationList = vacationList
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

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val day = listCalendarDayInfo[position]
        var convertView = view

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layout, parent, false)
        }

        var cell: TextView?
        if (layout == R.layout.dialog_calendar_cell) {

            cell = convertView?.calendar_cell
            val selectedCell = convertView?.calendar_selected
            val rangesCell = convertView?.calendar_ranges
            val rangesCell2 = convertView?.calendar_ranges2

            selectedCell?.visibility = View.GONE
            rangesCell?.visibility = View.GONE
            rangesCell2?.visibility = View.GONE

            cell?.text = day.getDay()
//        val now = (LocalDate.now().toString().replace("-", "")).toInt()
//        val selected = (day.getDate().toString().replace("-", "")).toInt()
//        if (isFutureSelect || now >= selected) {
            if (isSelectedRanges && firstPosition != -1 && lastPosition != -1) {
                val fP = min(firstPosition, lastPosition)
                val lP = max(firstPosition, lastPosition)
                if (position in fP..lP) {
                    if (position == fP) {
                        rangesCell?.visibility = View.INVISIBLE
                        rangesCell2?.visibility = View.VISIBLE
                        selectedCell?.visibility = View.VISIBLE
                    } else if (position == lP) {
                        rangesCell?.visibility = View.VISIBLE
                        rangesCell2?.visibility = View.INVISIBLE
                        selectedCell?.visibility = View.VISIBLE
                    } else {
                        rangesCell?.visibility = View.VISIBLE
                        rangesCell2?.visibility = View.GONE
                    }
                }
            }

//            else {
//                cell?.setOnClickListener {
//
//                    day.getDate()?.let {
//
//                        if (isSelectedMulti) {
//                            if (selectedDateArray.contains(it)) {
//                                selectedDateArray.remove(it)
//                            } else {
//                                selectedDateArray.add(it)
//                            }
//                        } else {
//                            selectedDateArray.clear()
//                            selectedDateArray.add(it)
//                        }
//
//                    }
//                    selectedDateClickListener?.onSelectedDateClick(selectedDateArray)
//                    notifyDataSetChanged()
//                }
//            }
//        }
//
//        if (firstPosition == -1 && lastPosition == -1) {
//            selectedCell?.visibility = if (day.isSameDay(selectedDateArray)) View.VISIBLE else View.INVISIBLE
//        }

            if (firstPosition == -1 && lastPosition == -1
                    && day.isSameDay(selectedDateArray)) {

                selectedCell?.visibility = View.VISIBLE

                if (selectedDateArray.size > 1 && isSelectedRanges) {
                    if (day.getDate()?.equals(selectedDateArray[0]) == true) {
                        rangesCell?.visibility = View.INVISIBLE
                        rangesCell2?.visibility = View.VISIBLE
                    } else if (day.getDate()?.equals(selectedDateArray[selectedDateArray.size - 1]) == true) {
                        rangesCell?.visibility = View.VISIBLE
                        rangesCell2?.visibility = View.INVISIBLE
                    } else {
                        rangesCell?.visibility = View.VISIBLE
                        rangesCell2?.visibility = View.GONE
                        selectedCell?.visibility = View.GONE
                    }
                }
            }


        } else
//            if (layout == R.layout.viewpager_worker_main_calendar_cell)
        {
            cell = convertView?.viewpagerWorkerMain_cell
            val imgVacation = convertView?.viewpagerWorkerMain_imgVacation
            val vacationInfo = UICommonUtil.getVacationInfo(day.getDate().toString(), vacationList)

            if (vacationInfo != null) {
                imgVacation?.visibility = View.VISIBLE
            } else {
                imgVacation?.visibility = View.GONE
            }


        }
        cell?.text = day.getDay()
        if (day.isSameDay(LocalDate.now())) {
            cell?.paintFlags = UNDERLINE_TEXT_FLAG
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


    /**
     * 날짜선택
     */
    fun setSelectedDate(selectedDateArray: ArrayList<LocalDate>) {
        this.selectedDateArray = selectedDateArray
        notifyDataSetChanged()
    }

    /**
     * 날짜범위선택
     */
    fun setRangesPosition(firstPosition: Int, lastPosition: Int) {
        this.firstPosition = firstPosition
        this.lastPosition = lastPosition
        notifyDataSetChanged()
    }

    /**
     * 현재날짜포지션
     */
    fun getCurrentDatePosition(): Int {
        return currentPosition
    }
}