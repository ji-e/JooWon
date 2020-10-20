package com.example.uohih.joowon.ui.customView

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.DialogCalendarBinding
import com.example.uohih.joowon.ui.adapter.CalendarAdapter
import com.example.uohih.joowon.util.DateCommonUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 캘린더 다이얼로그
 * context: Context
 * theme: Int
 */
class CalendarDialog(mContext: Context) : BaseBottomDialog(mContext), View.OnClickListener {

    private var bind: DialogCalendarBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_calendar, null, false)

    private lateinit var calendarAdapter: CalendarAdapter

    private lateinit var layoutBottom: CoordinatorLayout
    private lateinit var btnClose: ImageButton
    private lateinit var tvDate: TextView
    private lateinit var imgTri: ImageView
    private lateinit var layoutDay: LinearLayout
    private lateinit var gridView: GridView
    private lateinit var pickerY: NumberPicker
    private lateinit var pickerM: NumberPicker
    private lateinit var btnConfirm: Button


    private var calendar = LocalDate.now()
    private var selectedDate = LocalDate.now()
    private val todayJson = DateCommonUtil().getToday()

    private var isFutureSelect = false
    private var isVisibleCalendar = true

    private var mCloseBtnClickListener: View.OnClickListener? = null
    private var mConfirmBtnClickListener: ConfirmBtnClickListener? = null
    private lateinit var mItemClickListener: AdapterView.OnItemClickListener

    interface ConfirmBtnClickListener {
        fun onConfirmClick(date: LocalDate)
    }

    fun setCloseBtnClickListener(mCloseBtnClickListener: View.OnClickListener) {
        this.mCloseBtnClickListener = mCloseBtnClickListener
    }

    fun setConfirmBtnClickListener(mConfirmBtnClickListener: ConfirmBtnClickListener) {
        this.mConfirmBtnClickListener = mConfirmBtnClickListener
    }

    init {
        bind.run {
            lifecycleOwner = mContext as LifecycleOwner
            setContentView(bind.root)
        }

        initView()
    }

    private fun initView() {
        layoutBottom = bind.calendarLayout
        btnClose = bind.calendarBtnClose
        tvDate = bind.calendarTvDate
        imgTri = bind.calendarImgTri
        layoutDay = bind.calendarLayDay
        gridView = bind.calendarGridview
        pickerY = bind.calendarPickerY
        pickerM = bind.calendarPickerM
        btnConfirm = bind.calendarBtnConfirm

        btnClose.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)

        setDatePicker()

        gridView.setOnItemClickListener { parent, view, position, id ->

            val selectedDate = (view.tag as CalendarDayInfo).getDate()
            if (selectedDate?.format(DateTimeFormatter.ofPattern("yyyyMMdd"))?.toInt() ?: 0
                    <= todayJson.get("yyyymmdd").toString().toInt()
                    || isFutureSelect) {

                selectedDate?.let { setSelectedDate(it) }

                calendarAdapter.notifyDataSetChanged()
            }

        }

//        setObserve()
    }


    /**
     * picker 설정
     */
    private fun setDatePicker() {
        val year = todayJson.get("year").toString().toInt()
        val month = todayJson.get("month").toString().toInt()

        pickerY.wrapSelectorWheel = false
        pickerM.wrapSelectorWheel = false

        pickerY.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        pickerM.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        pickerY.minValue = 1900
        pickerM.minValue = 1

        pickerY.maxValue = year
        pickerM.maxValue = 12

        pickerY.value = year
        pickerM.value = month

        tvDate.text = year.toString() + "." + String.format("%02d", month)

//        year.setDisplayedValues(arrayOf("2019년", "2020년"))
    }

    /**
     * Calendar / Picker Layout
     */
    private fun setVisibleLayout() {
        if (isVisibleCalendar) {
            // calendar
            gridView.visibility = View.VISIBLE
            layoutDay.visibility = View.VISIBLE
            pickerY.visibility = View.GONE
            pickerM.visibility = View.GONE
            btnConfirm.visibility = View.VISIBLE
            imgTri.setImageResource(R.drawable.btn_dropdown_tri)
        } else {
            // picker
            gridView.visibility = View.INVISIBLE
            layoutDay.visibility = View.GONE
            pickerY.visibility = View.VISIBLE
            pickerM.visibility = View.VISIBLE
            btnConfirm.visibility = View.GONE
            imgTri.setImageResource(R.drawable.btn_up_tri)
        }
    }

    /**
     * 그리드 뷰 아이템 리스너
     */
    fun setmItemClickListener(mItemClickListener: AdapterView.OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    /**
     * 그리드 뷰
     */
    fun setGridAdapter(calendarAdapter: CalendarAdapter) {
        this.calendarAdapter = calendarAdapter
    }


    /**
     * 선택 날짜
     */
    private fun setSelectedDate(date: LocalDate) {
        selectedDate = date
        calendarAdapter.selectedDate = date
    }

    private fun setGridCalendar(date: String) {
        calendar = LocalDate.parse(date)
        getCalendar(calendar)
        setSelectedDate(calendar)
    }

    private fun getCalendar(dateForCurrentMonth: LocalDate) {
        setVisibleLayout()

        calendarAdapter = CalendarAdapter(mContext, JWBaseActivity().getCalendar(dateForCurrentMonth), selectedDate, R.layout.dialog_calendar_cell)
        gridView.adapter = calendarAdapter
    }


    fun setBottomDialog(date: String,
                        onCloseListener: View.OnClickListener?,
                        onConfirmListener: ConfirmBtnClickListener?) {

        onCloseListener?.let { setCloseBtnClickListener(it) }
        onConfirmListener?.let { setConfirmBtnClickListener(it) }


        // 다이얼로그 높이 설정
        layoutBottom.viewTreeObserver.addOnGlobalLayoutListener {
            var height = layoutBottom.height
            height = SizeConverterUtil(mContext).px(height.toFloat())
            setPeekHeight(height.toFloat())
        }

        setGridCalendar(date)

//        setCancelable(false)
    }

    override fun onClick(v: View) {
        when (v) {
            btnClose -> {
                mCloseBtnClickListener?.let {
                    btnClose.setOnClickListener(it)
                }
                dismiss()
            }
            tvDate -> {
                isVisibleCalendar = !isVisibleCalendar
                setVisibleLayout()

                if (isVisibleCalendar) {
                    val year = pickerY.value
                    val month = pickerM.value
                    calendar = calendar.withYear(year)
                    calendar = calendar.withMonth(month)
                    getCalendar(calendar)

                    tvDate.text = year.toString() + "." + String.format("%02d", month)
                }
            }
            btnConfirm -> {
                mConfirmBtnClickListener?.run {
                    onConfirmClick(calendar)
                }
                dismiss()
            }
        }
    }
}










