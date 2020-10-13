package com.example.uohih.joowon.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.uohih.joowon.ui.customView.CalendarDayInfo
import com.example.uohih.joowon.database.VacationData
import kotlinx.android.synthetic.main.grid_item_worker_main.view.*
import java.time.LocalDate

import java.util.*
import kotlin.collections.ArrayList


/**
 * 캘린더 아답터
 * mContext: Context
 * arrayListDayInfo: ArrayList<CalendarDayInfo>: 캘린더 날짜 정보 리스트
 * date: Date
 */
class CalendarAdapter(private val mContext: Context, listDayInfo: ArrayList<CalendarDayInfo>,
                      val date: LocalDate, val layout: Int) : BaseAdapter() {
    var selectedDate = date
    var arrayListDayInfo = listDayInfo

    // 리스트 뷰
    private var vacationList = arrayListOf<VacationData>()

    override fun getCount(): Int {
        return arrayListDayInfo.size
    }

    override fun getItem(position: Int): Any? {
        return if (position >= count) null else arrayListDayInfo[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val day = arrayListDayInfo[position]
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layout, parent, false)
        }

        // 캘린더 날짜
        val cell = convertView?.calendar_cell
        // 오늘 동그라미 이미지
        val today = convertView?.calendar_today

        val halfV = convertView?.calendar_half_v
        val allV = convertView?.calendar_all_v


        /**
         * ------------- 날짜 그리기 start -------------
         */

        cell?.text = day.getDay()
        if (day.isSameDay(selectedDate)) {
            today?.visibility = View.VISIBLE
        } else {
            today?.visibility = View.INVISIBLE
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


        if (day.isInMonth()) {
            when {
                position % 7 + 1 == Calendar.SUNDAY -> cell?.setTextColor(Color.RED)
                position % 7 + 1 == Calendar.SATURDAY -> cell?.setTextColor(Color.BLUE)
                else -> cell?.setTextColor(Color.BLACK)
            }
        } else {
            cell?.setTextColor(Color.GRAY)
        }
//        }
        /**
         * ------------- 날짜 그리기 end -------------
         */

        convertView?.tag = day



        return convertView!!

    }

    fun setVacationData(vacationList: ArrayList<VacationData>) {
        this.vacationList = vacationList
        notifyDataSetChanged()

    }

    fun setListDayInfo(arrayListDayInfo: ArrayList<CalendarDayInfo>) {
        this.arrayListDayInfo = arrayListDayInfo
        notifyDataSetChanged()
    }



}