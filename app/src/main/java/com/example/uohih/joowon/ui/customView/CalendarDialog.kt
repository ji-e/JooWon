package com.example.uohih.joowon.ui.customView

import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.uohih.joowon.R
//import com.example.uohih.joowon.databinding.DialogCalendarBinding
import com.example.uohih.joowon.ui.adapter.CalendarAdapter
import com.example.uohih.joowon.util.SizeConverterUtil
import kotlinx.android.synthetic.main.dialog_calendar.*

/**
 * 캘린더 다이얼로그
 * context: Context
 * theme: Int
 */
class CalendarDialog(mContext: Context) : BaseBottomDialog(mContext), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    private lateinit var calendarAdapter: CalendarAdapter
    private var layoutBottom: CoordinatorLayout
//    private var gridView: GridView
//    private var tvDate: TextView

    init {
        setContentView(R.layout.dialog_calendar)
//        val bind = DataBindingUtil.setContentView<DialogCalendarBinding>(mContext as Activity, R.layout.dialog_calendar)
        layoutBottom = calendar_layout
//        gridView = bind.calendarGridview
//        tvDate = bind.calendarTvDate
    }

    fun setBottomDialog(title: String, content: String?) {

//        // 타이틀 설정
//        titleView.setText(title)
//
//        // 내용 설정
//        if (content != null) {
//            contentView.setText(content)
//        } else {
//            contentView.setVisibility(View.GONE)
//        }
//
//        if (content2 != null) {
//            contentView2.setVisibility(View.VISIBLE)
//            contentView2.setText(content2)
//        }
//
//
//        // top 버튼 설정
//        if (btn1 != null) {
//            bottomBtn1.setText(btn1)
//        } else {
//            bottomBtn1.setVisibility(View.GONE)
//        }
//
//        // bottom 버튼 설정
//        if (btn2 != null) {
//            bottomBtn2.setText(btn2)
//        } else {
//            bottomBtn2.setVisibility(View.GONE)
//        }
//
//        if (cancel) {
//            btn_cancel.setVisibility(View.VISIBLE)
//        } else {
//            btn_cancel.setVisibility(View.GONE)
//        }
//
//        if (primary) {
//            dialog_primary.setVisibility(View.VISIBLE)
//        }
//
//
//        // 버튼 리스너 설정
//        setTMCustomBottomDialogListener(tmCustomBottomDialogListener)

        // 다이얼로그 높이 설정
        layoutBottom.viewTreeObserver.addOnGlobalLayoutListener {
            var height = layoutBottom.height
            height = SizeConverterUtil(mContext).px(height.toFloat())
            setPeekHeight(height.toFloat())
        }

//        setCancelable(false)
    }

//    class Builder(private val mContext: Context, mFuture: Boolean) : View.OnClickListener {
//
//        private lateinit var dialog: CalendarDialog
//        private var dialogTitle = ""
//        private var close = false
//        private var future = mFuture
//
//        private lateinit var mCloseBtnClickListener: DialogInterface.OnClickListener
//        private lateinit var mItemClickListener: AdapterView.OnItemClickListener
//
//        private lateinit var calendarAdapter: CalendarAdapter
//        private lateinit var gridView: GridView
//
//        private val todayJson = DateCommonUtil().getToday()
//        private var date = ""
//        private var calendar = LocalDate.now()
//        private var selectedDate = LocalDate.now()
//
//        private lateinit var arrayListDayInfo: ArrayList<CalendarDayInfo>
//
////        private lateinit var tvYear: TextView
////        private lateinit var tvMonth: TextView
//
//        val mThisMonthCalendar = LocalDate.now()
//
//        private lateinit var tvDate: TextView
//
//        /**
//         * 날짜 세팅
//         */
//        fun setDate(text: String): Builder {
//            date = text
//            return this
//        }
//
//
//        /**
//         * 닫기 버튼 리스너
//         */
//        fun setmCloseBtnClickListener(text: String, mCloseBtnClickListener: DialogInterface.OnClickListener): Builder {
//            if (mCloseBtnClickListener != null) {
//                close = true
//            }
//            dialogTitle = text
//            this.mCloseBtnClickListener = mCloseBtnClickListener
//            return this
//        }
//
//        /**
//         * 그리드 뷰 아이템 리스너
//         */
//        fun setmItemClickListener(mItemClickListener: AdapterView.OnItemClickListener): Builder {
//            this.mItemClickListener = mItemClickListener
//            return this
//        }
//
//        /**
//         * 그리드 뷰
//         */
//        fun setGridAdapter(calendarAdapter: CalendarAdapter): Builder {
//            this.calendarAdapter = calendarAdapter
//            return this
//        }
//
//
//        /**
//         * 선택 날짜
//         */
//
//        private fun setSelectedDate(date: LocalDate) {
//            selectedDate = date
//
//            if (calendarAdapter != null) {
//                calendarAdapter.selectedDate = date
//            }
//        }
//
//
//        /**
//         * 커스텀 다이얼로그 생성
//         */
//
//        fun create(): CalendarDialog {
//            val contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_calendar, null)
//            dialog = CalendarDialog((mContext as Activity), android.R.style.Theme_Material_Dialog_MinWidth).apply {
//                requestWindowFeature(Window.FEATURE_NO_TITLE)
//                addContentView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
//            }
//
//            arrayListDayInfo = ArrayList<CalendarDayInfo>()
//
////            tvYear = contentView.calendar_tv_year
////            tvMonth = contentView.calendar_tv_month
////            // 현재 날짜 세팅
////            tvYear.text = todayJson.getString("year")
////            tvMonth.text = todayJson.getString("month")
//
//
//            gridView = contentView.calendar_gridview
//            tvDate = contentView.calendar_tvDate
//
//
//
//            contentView.calendar_btnClose.setOnClickListener(this)
//            contentView.calendar_tvDate.setOnClickListener(this)
//            contentView.calendar_btnConfirm.setOnClickListener(this)
//
//
////            // 년 이전버튼
////            contentView.calendar_btn_backy.setOnClickListener(this)
////
////            // 년 다음버튼
////            contentView.calendar_btn_nexty.setOnClickListener(this)
////
////            // 월 이전버튼
////            contentView.calendar_btn_backm.setOnClickListener(this)
////
////            // 월 다음버튼
////            contentView.calendar_btn_nextm.setOnClickListener(this)
//
//
//            // 그리드뷰 세팅
//
//            setGridCalendar()
////            contentView.calendar_tv_month.text = String.format("%02d", calendar.monthValue)
////            contentView.calendar_tv_year.text = calendar.year.toString()
//
//            return dialog
//        }
//
//        private fun setGridCalendar() {
////            getCalendar(mThisMonthCalendar)
//            calendar = LocalDate.parse(date)
//            getCalendar(calendar)
//            setSelectedDate(calendar)
//
//            tvDate.text = calendar.year.toString() + "." + calendar.monthValue.toString()
//
//            gridView.setOnItemClickListener { parent, view, position, id ->
//
//                val selectedDate = (view.tag as CalendarDayInfo).getDate()
//                if (selectedDate?.format(DateTimeFormatter.ofPattern("yyyyMMdd"))?.toInt() ?: 0
//                        <= todayJson.get("yyyymmdd").toString().toInt()
//                        || future) {
//
//                    selectedDate?.let { setSelectedDate(it) }
//
//                    calendarAdapter.notifyDataSetChanged()
//                }
//
//            }
//
//        }
//
//
//        private fun getCalendar(dateForCurrentMonth: LocalDate) {
//            calendarAdapter = CalendarAdapter(mContext, JWBaseActivity().getCalendar(dateForCurrentMonth), selectedDate, R.layout.dialog_calendar_cell)
//            gridView.adapter = calendarAdapter
//        }
//
//        override fun onClick(view: View) {
//            when (view.id) {
//                R.id.calendar_btnClose -> {
//                    dialog.dismiss()
//                }
//                R.id.calendar_tvDate -> {
//
//                }
////                R.id.calendar_btn_backy -> {
////                    calendar = calendar.minusYears(1)
////                    getCalendar(calendar)
////                    tvYear.text = calendar.year.toString()
////                }
////                R.id.calendar_btn_nexty -> {
////                    if (tvYear.text.toString().toInt() < todayJson.getString("year").toInt()
////                            || future) {
////                        calendar = calendar.plusYears(1)
////                        getCalendar(calendar)
////                        tvYear.text = calendar.year.toString()
////                    }
////                }
////                R.id.calendar_btn_backm -> {
////                    calendar = calendar.minusMonths(1)
////                    getCalendar(calendar)
////                    tvMonth.text = String.format("%02d", calendar.monthValue)
////                    tvYear.text = calendar.year.toString()
////                }
////                R.id.calendar_btn_nextm -> {
////                    if (tvYear.text.toString().toInt() < todayJson.getString("year").toInt()
////                            || tvMonth.text.toString().toInt() < todayJson.getString("month").toInt()
////                            || future) {
////                        calendar = calendar.plusMonths(1)
////                        getCalendar(calendar)
////                        tvMonth.text = String.format("%02d", calendar.monthValue)
////                        tvYear.text = calendar.year.toString()
////                    }
////                }
//                R.id.calendar_btnConfirm -> {
//                    val base = JWBaseApplication()
////                base.setSelectDate(selectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
//                    base.setSelectDate(selectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
//                    dialog.dismiss()
//                }
//            }
//        }
//
//
//    }
//
//
//    /**
//     * 캘린더 다이얼로그
//     */
//
//    fun createDialogCalendar(mContext: Context, date: String?): CalendarDialog? {
//        return createDialogCalendar(mContext, date, false)
//    }
//
//    fun createDialogCalendar(mContext: Context, date: String?, future: Boolean): CalendarDialog? {
//        if (mContext is Activity) {
//            val activity = mContext as Activity?
//
//            if (activity!!.isFinishing || activity.isDestroyed) {
//                return null
//            }
//        }
//
//
//        val builder = Builder(mContext, future)
//        if (date != null)
//            builder.setDate(date)
//        return builder.create()
//    }
}








