package com.example.uohih.joowon.ui.customView

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.R
import com.example.uohih.joowon.ui.adapter.CalendarAdapter
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.util.DateCommonUtil
import kotlinx.android.synthetic.main.dialog_calendar.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 캘린더 다이얼로그
 * context: Context
 * theme: Int
 */
class CalendarDialog(mContext: Context, theme: Int) : Dialog(mContext, theme) {

    class Builder(private val mContext: Context, mFuture: Boolean) : View.OnClickListener {

        private lateinit var dialog: CalendarDialog
        private var dialogTitle = ""
        private var close = false
        private var future = mFuture

        private lateinit var mCloseBtnClickListener: DialogInterface.OnClickListener
        private lateinit var mItemClickListener: AdapterView.OnItemClickListener

        private lateinit var calendarAdapter: CalendarAdapter
        private lateinit var gridView: GridView

        private val todayJson = DateCommonUtil().getToday()
        private var date = ""
        private var calendar = LocalDate.now()
        private var selectedDate = LocalDate.now()

        private lateinit var arrayListDayInfo: ArrayList<CalendarDayInfo>

        private lateinit var tvYear: TextView
        private lateinit var tvMonth: TextView


        /**
         * 날짜 세팅
         */
        fun setDate(text: String): Builder {
            date = text
            return this
        }


        /**
         * 닫기 버튼 리스너
         */
        fun setmCloseBtnClickListener(text: String, mCloseBtnClickListener: DialogInterface.OnClickListener): Builder {
            if (mCloseBtnClickListener != null) {
                close = true
            }
            dialogTitle = text
            this.mCloseBtnClickListener = mCloseBtnClickListener
            return this
        }

        /**
         * 그리드 뷰 아이템 리스너
         */
        fun setmItemClickListener(mItemClickListener: AdapterView.OnItemClickListener): Builder {
            this.mItemClickListener = mItemClickListener
            return this
        }

        /**
         * 그리드 뷰
         */
        fun setGridAdapter(calendarAdapter: CalendarAdapter): Builder {
            this.calendarAdapter = calendarAdapter
            return this
        }


        /**
         * 선택 날짜
         */

        private fun setSelectedDate(date: LocalDate) {
            selectedDate = date

            if (calendarAdapter != null) {
                calendarAdapter.selectedDate = date
            }
        }


        /**
         * 커스텀 다이얼로그 생성
         */

        fun create(): CalendarDialog {
            val contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_calendar, null)
            dialog = CalendarDialog((mContext as Activity), android.R.style.Theme_Material_Dialog_MinWidth).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                addContentView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            }
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)


//            dialog.addContentView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            arrayListDayInfo = ArrayList<CalendarDayInfo>()
            val mThisMonthCalendar = LocalDate.now()

            tvYear = contentView.calendar_tv_year
            tvMonth = contentView.calendar_tv_month

            // 현재 날짜 세팅
            tvYear.text = todayJson.getString("year")
            tvMonth.text = todayJson.getString("month")


            // 닫기 버튼
            contentView.calendar_btn_close.setOnClickListener(this)

            // 년 이전버튼
            contentView.calendar_btn_backy.setOnClickListener(this)

            // 년 다음버튼
            contentView.calendar_btn_nexty.setOnClickListener(this)

            // 월 이전버튼
            contentView.calendar_btn_backm.setOnClickListener(this)

            // 월 다음버튼
            contentView.calendar_btn_nextm.setOnClickListener(this)

            /**
             * 날짜 클릭
             */
            contentView.calendar_gridview.setOnItemClickListener { parent, view, position, id ->

                val selectedDate = (view.tag as CalendarDayInfo).getDate()
//                LogUtil.e(selectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString())
                if (selectedDate?.format(DateTimeFormatter.ofPattern("yyyyMMdd"))!!.toInt() <= todayJson.get("yyyymmdd").toString().toInt()
                        || future) {

                    setSelectedDate(selectedDate)
                    calendarAdapter.notifyDataSetChanged()
                }

            }

            // 확인버튼 클릭
            contentView.calendar_btn_confirm.setOnClickListener(this)

            // 그리드뷰 세팅
            gridView = contentView.calendar_gridview
            getCalendar(mThisMonthCalendar)
            if (date != null) {
//                val df = SimpleDateFormat("yyyy-MM-dd")
                LogUtil.d(date)
//                val d = df.parse(date)
//                val localDate = LocalDate.of((date?.substring(0,4))!!.toInt(), (date?.substring(5,7))!!.toInt(), (date?.substring(8,10))!!.toInt())
                calendar = LocalDate.parse(date)
                setSelectedDate(calendar)
                getCalendar(calendar)
                contentView.calendar_tv_month.text = String.format("%02d", calendar.monthValue)
                contentView.calendar_tv_year.text = calendar.year.toString()
            } else {
                setSelectedDate(LocalDate.now())
            }

            return dialog
        }


        private fun getCalendar(dateForCurrentMonth: LocalDate) {
            calendarAdapter = CalendarAdapter(mContext, JWBaseActivity().getCalendar(dateForCurrentMonth), selectedDate, R.layout.dialog_calendar_cell)
            gridView.adapter = calendarAdapter
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.calendar_btn_close -> {
                    dialog.dismiss()
                }
                R.id.calendar_btn_backy -> {
                    calendar = calendar.minusYears(1)
                    getCalendar(calendar)
                    tvYear.text = calendar.year.toString()
                }
                R.id.calendar_btn_nexty -> {
                    if (tvYear.text.toString().toInt() < todayJson.getString("year").toInt()
                            || future) {
                        calendar = calendar.plusYears(1)
                        getCalendar(calendar)
                        tvYear.text = calendar.year.toString()
                    }
                }
                R.id.calendar_btn_backm -> {
                    calendar = calendar.minusMonths(1)
                    getCalendar(calendar)
                    tvMonth.text = String.format("%02d", calendar.monthValue)
                    tvYear.text = calendar.year.toString()
                }
                R.id.calendar_btn_nextm -> {
                    if (tvYear.text.toString().toInt() < todayJson.getString("year").toInt()
                            || tvMonth.text.toString().toInt() < todayJson.getString("month").toInt()
                            || future) {
                        calendar = calendar.plusMonths(1)
                        getCalendar(calendar)
                        tvMonth.text = String.format("%02d", calendar.monthValue)
                        tvYear.text = calendar.year.toString()
                    }
                }
                R.id.calendar_btn_confirm -> {
                    val base = JWBaseApplication()
//                base.setSelectDate(selectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    base.setSelectDate(selectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    dialog.dismiss()
                }
            }
        }


    }




    /**
     * 캘린더 다이얼로그
     */

    fun createDialogCalendar(mContext: Context, date: String?): CalendarDialog? {
        return createDialogCalendar(mContext, date, false)
    }

    fun createDialogCalendar(mContext: Context, date: String?, future: Boolean): CalendarDialog? {
        if (mContext is Activity) {
            val activity = mContext as Activity?

            if (activity!!.isFinishing || activity.isDestroyed) {
                return null
            }
        }


        val builder = Builder(mContext, future)
        if (date != null)
            builder.setDate(date)
        return builder.create()
    }
}








