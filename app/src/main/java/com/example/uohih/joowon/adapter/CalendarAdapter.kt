package com.example.uohih.joowon.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import com.example.uohih.joowon.view.CalendarDayInfo
import com.example.uohih.joowon.R
import kotlinx.android.synthetic.main.grid_item_worker_main.view.*

import java.util.*


/**
 * 캘린더 아답터
 * mContext: Context
 * arrayListDayInfo: ArrayList<CalendarDayInfo>: 캘린더 날짜 정보 리스트
 * date: Date
 */
class CalendarAdapter(private val mContext: Context, private val arrayListDayInfo: ArrayList<CalendarDayInfo>,
                      val date: Date, val layout:Int) : BaseAdapter() {
    var selectedDate = date

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


        /**
         * ------------- 날짜 그리기 start -------------
         */
//        if (day != null) {
        cell?.text = day.getDay()

        if (day.isSameDay(selectedDate)) {
            today?.visibility = View.VISIBLE
        } else {
            today?.visibility = View.INVISIBLE
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

}