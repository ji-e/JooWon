package com.example.uohih.joowon.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.GridView
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.CalendarAdapter
import com.example.uohih.joowon.base.LogUtil
import kotlinx.android.synthetic.main.dialog_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 캘린더 다이얼로그
 * context: Context
 * theme: Int
 */
class CalendarDialog(context: Context, theme: Int) : Dialog(context, theme) {

    class Builder(private val context: Context) {
        lateinit var dialogTitle: String
        private var close = false

        lateinit var mClosebtnClickListener: DialogInterface.OnClickListener
        lateinit var mItemClickListener: AdapterView.OnItemClickListener

        lateinit var calendarAdapter: CalendarAdapter
        lateinit var gridView: GridView

        private val todayJson = JWBaseActivity().getToday()
        private var date: String? = null

        private val instance = Calendar.getInstance()
        private var sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        lateinit var arrayListDayInfo: ArrayList<CalendarDayInfo>
        private var selectedDate: Date = Date()


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
        fun setmCloseBtnClickListener(text: String, mClosebtnClickListener: DialogInterface.OnClickListener): Builder {
            if (mClosebtnClickListener != null) {
                close = true
            }
            dialogTitle = text
            this.mClosebtnClickListener = mClosebtnClickListener
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
        fun setSelectedDate(date: Date?) {
            selectedDate = date!!

            if (calendarAdapter != null) {
                calendarAdapter.selectedDate = date
            }
        }



        /**
         * 커스텀 다이얼로그 생성
         */
        fun create(): CalendarDialog {
            val dialog = CalendarDialog((context as Activity), android.R.style.Theme_Material_Dialog_MinWidth)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_calendar, null)

            dialog.addContentView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            arrayListDayInfo = ArrayList<CalendarDayInfo>()
            val mThisMonthCalendar = Calendar.getInstance()

            /**
             * 현재 날짜 세팅
             */
            contentView.calendar_tv_year.text = todayJson.getString("year")
            contentView.calendar_tv_month.text = todayJson.getString("month")


            /**
             * 닫기 버튼
             */
            contentView.calendar_btn_close.setOnClickListener {
                dialog.dismiss()
            }

            /**
             * 년 이전버튼
             */
            contentView.calendar_btn_backy.setOnClickListener {
                instance.add(Calendar.YEAR, -1)
                getCalendar(instance.time)
                contentView.calendar_tv_year.text = instance.get(Calendar.YEAR).toString()
            }

            /**
             * 년 다음버튼
             */
            contentView.calendar_btn_nexty.setOnClickListener {
                if (contentView.calendar_tv_year.text.toString().toInt() < todayJson.getString("year").toInt()) {
                    instance.add(Calendar.YEAR, +1)
                    getCalendar(instance.time)
                    contentView.calendar_tv_year.text = instance.get(Calendar.YEAR).toString()
                }
            }


            /**
             * 월 이전버튼
             */
            contentView.calendar_btn_backm.setOnClickListener {
                instance.add(Calendar.MONTH, -1)
                getCalendar(instance.time)
                contentView.calendar_tv_month.text = String.format("%02d", instance.get(Calendar.MONTH) + 1)
                contentView.calendar_tv_year.text = instance.get(Calendar.YEAR).toString()
            }

            /**
             * 월 다음버튼
             */
            contentView.calendar_btn_nextm.setOnClickListener {
                if (contentView.calendar_tv_year.text.toString().toInt() < todayJson.getString("year").toInt() || contentView.calendar_tv_month.text.toString().toInt() < todayJson.getString("month").toInt()) {
                    instance.add(Calendar.MONTH, +1)
                    getCalendar(instance.time)
                    contentView.calendar_tv_month.text = String.format("%02d", instance.get(Calendar.MONTH) + 1)
                    contentView.calendar_tv_year.text = instance.get(Calendar.YEAR).toString()
                }
            }

            /**
             * 날짜 클릭
             */
            contentView.calendar_gridview.setOnItemClickListener { parent, view, position, id ->

                val selectedDate = (view.tag as CalendarDayInfo).getDate()
                if ((sdf.format(selectedDate)).toInt() <= todayJson.get("yyyymmdd").toString().toInt()) {
                    setSelectedDate(selectedDate)
                    calendarAdapter.notifyDataSetChanged()
                }

            }

            /**
             * 확인버튼 클릭
             */
            contentView.calendar_btn_confirm.setOnClickListener {
                val base = JWBaseApplication()
                base.setSeleteDate(sdf.format(selectedDate))
                dialog.dismiss()
            }

            // 그리드뷰 세팅
            gridView = contentView.calendar_gridview
            getCalendar(mThisMonthCalendar.time)
            if (date != null) {
                val df = SimpleDateFormat("yyyy-MM-dd")
                LogUtil.d(date!!)
                val d = df.parse(date)
                setSelectedDate(d)
            } else {
                setSelectedDate(Date())
            }
            return dialog
        }

        fun getCalendar(dateForCurrentMonth: Date){
            calendarAdapter = CalendarAdapter(context, JWBaseActivity().getCalendar(dateForCurrentMonth), selectedDate, R.layout.dialog_calendar_cell)
            gridView.adapter = calendarAdapter
        }
    }




    /**
     * 캘린더 다이얼로그
     */
    fun showDialogCalendar(context: Context, date: String?): CalendarDialog? {
        if (context is Activity) {
            val activity = context as Activity?

            if (activity!!.isFinishing || activity.isDestroyed) {
                return null
            }
        }


        val builder = CalendarDialog.Builder(context)
        if (date != null)
            builder.setDate(date)
        val dialog = builder.create()
        return dialog
    }


}





