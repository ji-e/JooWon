package com.example.uohih.joowon.ui.customView


import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.uohih.joowon.BR
import com.example.uohih.joowon.R
import com.example.uohih.joowon.databinding.DialogCalendarBinding
import com.example.uohih.joowon.databinding.DialogCalendarGridBinding
import com.example.uohih.joowon.model.CalendarDayInfo
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.adapter.CalendarAdapter
import com.example.uohih.joowon.util.DateCommonUtil
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import kotlinx.android.synthetic.main.dialog_calendar_grid.view.*
import java.time.LocalDate
import kotlin.math.max
import kotlin.math.min


/**
 * 캘린더 다이얼로그
 * context: Context
 * theme: Int
 */
class CalendarDialog(mContext: Context) : BaseBottomDialog(mContext), View.OnClickListener {

    private var bind: DialogCalendarBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_calendar, null, false)
    private var calendarDialogViewModel = CalendarDialogViewModel()

    private lateinit var layoutBottom: CoordinatorLayout
    private lateinit var btnClose: ImageButton
    private lateinit var tvDate: TextView
    private lateinit var imgTri: ImageView

    private lateinit var viewPager: ViewPager2
    private lateinit var pickerY: NumberPicker
    private lateinit var pickerM: NumberPicker
    private lateinit var btnConfirm: Button

    private var calendar = LocalDate.now()
    private var selectedDate = arrayListOf<LocalDate>()
    private val todayJson = DateCommonUtil().getToday()

    private var isFutureSelect = false
    private var isSelectedMulti = false
    private var isSelectedRang = false
    private var isVisibleCalendar = true
    private var previousPosition = 100

    private var mCloseBtnClickListener: View.OnClickListener? = null
    private var mConfirmBtnClickListener: ConfirmBtnClickListener? = null

    interface ConfirmBtnClickListener {
        fun onConfirmClick(date: ArrayList<LocalDate>)
    }

    init {
        bind.run {
            lifecycleOwner = mContext as LifecycleOwner
            setContentView(bind.root)
            calendarDialogVm = calendarDialogViewModel
        }

        initView()
    }

    private fun initView() {
        layoutBottom = bind.calendarLayout
        btnClose = bind.calendarBtnClose
        tvDate = bind.calendarTvDate
        imgTri = bind.calendarImgTri
        viewPager = bind.calendarViewpager
        pickerY = bind.calendarPickerY
        pickerM = bind.calendarPickerM
        btnConfirm = bind.calendarBtnConfirm

        btnClose.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        imgTri.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)

        setDatePicker()
        setVisibleLayout()

    }

    /**
     * calendar 설정
     */
    private fun setCalendar(date: LocalDate) {
        var calendarAdapter: CalendarAdapter? = null
        calendar = date

        calendarDialogViewModel.setCalendarList(calendar)

        calendarDialogViewModel.liveCalendarList.observe(mContext as LifecycleOwner, Observer {
            val liveCalendarList = it ?: return@Observer

            val viewPagerAdapter = object : BaseRecyclerView.Adapter<ArrayList<CalendarDayInfo>, DialogCalendarGridBinding>(
                    layoutResId = R.layout.dialog_calendar_grid,
                    bindingVariableId = BR.calendarInfo
            ) {

                override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<DialogCalendarGridBinding>, position: Int) {
                    super.onBindViewHolder(holder, position)
                    val gridview = holder.itemView.calendar_gridview

                    calendarAdapter = CalendarAdapter(
                            mContext,
                            R.layout.dialog_calendar_cell,
                            liveCalendarList[position],
                            selectedDate,
                            isFutureSelect,
                            isSelectedMulti,
                            isSelectedRang
                    )
                    gridview.adapter = calendarAdapter

                    // 날짜 선택 리스너
                    calendarAdapter?.selectedDateClickListener = object : CalendarAdapter.SelectedDateClickListener {
                        override fun onSelectedDateClick(date: ArrayList<LocalDate>) {
                            selectedDate = date
                            notifyDataSetChanged()
                        }
                    }


                    var prePosition = -1
                    if (isSelectedRang) {
                        gridview.setOnItemLongClickListener { adapterView, view, firstPosition, l ->
                            gridview.setOnTouchListener { view, motionEvent ->

                                val action = motionEvent.actionMasked
                                val currentXPosition = motionEvent.x
                                val currentYPosition = motionEvent.y
                                val lastPosition = gridview.pointToPosition(currentXPosition.toInt(), currentYPosition.toInt())

                                if (action == MotionEvent.ACTION_UP) {
                                    calendarAdapter?.setRangesPosition(-1, -1)
                                    gridview.setOnTouchListener(null)
                                    val fP = min(firstPosition, lastPosition)
                                    var lP = max(firstPosition, lastPosition)

                                    val now = (LocalDate.now().toString().replace("-", "")).toInt()
                                    val selected = (liveCalendarList[position][lP].getDate().toString().replace("-", "")).toInt()
                                    if (!isFutureSelect && now < selected) {
                                         calendarAdapter?.getCurrentDatePosition()?.let {  it1-> lP = it1 }
                                    }

                                    selectedDate.clear()

                                    for (i in fP..lP) {
                                        liveCalendarList[position][i].getDate()?.let { it1 -> selectedDate.add(it1) }
                                    }
                                    calendarAdapter?.setSelectedDate(selectedDate)
                                }

                                if (prePosition != lastPosition) {
                                    prePosition = lastPosition

                                    if (lastPosition != -1) {
                                        calendarAdapter?.setRangesPosition(firstPosition, lastPosition)
                                    }
                                    calendarAdapter?.notifyDataSetChanged()

                                }

                                false
                            }
                            false
                        }
                    }
                }
            }

            viewPager.adapter = viewPagerAdapter

            // 뷰페이저 가운데로 인덱스변경
            viewPager.visibility = View.INVISIBLE
            viewPager.post {
                viewPager.setCurrentItem(100, false)
                viewPager.visibility = View.VISIBLE
            }


            // 뷰페이저 페이징변환 리스너
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        previousPosition = viewPager.currentItem
                    }
                    viewPager.post {
                        viewPagerAdapter.notifyDataSetChanged()
                    }
                }

                override fun onPageSelected(position: Int) {
                    if (position == 0) return
                    calendar = (liveCalendarList[position])[20].getDate()
                    previousPosition = position
                    calendarAdapter?.setSelectedDate(selectedDate)
                    tvDate.text = calendar.year.toString() + "." + String.format("%02d", calendar.monthValue)

                }
            })
        })
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

        pickerY.setOnValueChangedListener { picker, oldVal, newVal ->
            tvDate.text = newVal.toString() + "." + String.format("%02d", pickerM.value)
        }

        pickerM.setOnValueChangedListener { picker, oldVal, newVal ->
            tvDate.text = pickerY.value.toString() + "." + String.format("%02d", newVal)
        }
//        year.setDisplayedValues(arrayOf("2019년", "2020년"))
    }

    /**
     * Calendar / Picker Layout
     */
    private fun setVisibleLayout() {
        if (isVisibleCalendar) {
            // calendar
            viewPager.visibility = View.VISIBLE
            pickerY.visibility = View.GONE
            pickerM.visibility = View.GONE
            btnConfirm.visibility = View.VISIBLE
            imgTri.setImageResource(R.drawable.btn_dropdown_tri)
        } else {
            // picker
            viewPager.visibility = View.INVISIBLE
            pickerY.visibility = View.VISIBLE
            pickerM.visibility = View.VISIBLE
            btnConfirm.visibility = View.GONE
            imgTri.setImageResource(R.drawable.btn_up_tri)
        }
    }

    fun setBottomDialog(date: String,
                        onCloseListener: View.OnClickListener?,
                        onConfirmListener: ConfirmBtnClickListener?) {
        setBottomDialog(date, onCloseListener, onConfirmListener, isFutureSelect = false, isSelectedMulti = false, isSelectedRang = false)

    }

    fun setBottomDialog(date: String,
                        onCloseListener: View.OnClickListener?,
                        onConfirmListener: ConfirmBtnClickListener?,
                        isFutureSelect: Boolean,
                        isSelectedMulti: Boolean,
                        isSelectedRang: Boolean) {

        onCloseListener?.let { mCloseBtnClickListener = it }
        onConfirmListener?.let { mConfirmBtnClickListener = it }

        this.isFutureSelect = isFutureSelect
        this.isSelectedMulti = isSelectedMulti
        this.isSelectedRang = isSelectedRang


        // 다이얼로그 높이 설정
        layoutBottom.viewTreeObserver.addOnGlobalLayoutListener {
            var bottomHeight = layoutBottom.height
            bottomHeight = SizeConverterUtil(mContext).px(bottomHeight.toFloat())
            setPeekHeight(bottomHeight.toFloat())
        }

        setCalendar(LocalDate.parse(date))

    }

    override fun onClick(v: View) {
        when (v) {
            btnClose -> {
                // 닫기버튼
                mCloseBtnClickListener?.let {
                    btnClose.setOnClickListener(it)
                }
                dismiss()
            }
            tvDate, imgTri -> {
                // 년월선택
                isVisibleCalendar = !isVisibleCalendar


                if (isVisibleCalendar) {
                    val year = pickerY.value
                    val month = pickerM.value
                    calendar = calendar.withYear(year)
                    calendar = calendar.withMonth(month)

                    setCalendar(calendar)

                } else {
                    pickerY.value = calendar.year
                    pickerM.value = calendar.monthValue
                }

                setVisibleLayout()
            }
            btnConfirm -> {
                // 확인버튼
                mConfirmBtnClickListener?.run {
                    onConfirmClick(selectedDate)
                }
                dismiss()
            }
        }
    }

}










