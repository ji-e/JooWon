package com.example.uohih.joowon.ui.worker


import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.uohih.joowon.BR
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivityWorkerMainBinding
import com.example.uohih.joowon.databinding.ViewpagerWorkerMainBinding
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.adapter.CalendarAdapter
import com.example.uohih.joowon.ui.adapter.WorkerVacationListAdapter
import com.example.uohih.joowon.util.SizeConverterUtil
import kotlinx.android.synthetic.main.viewpager_worker_main.view.*
import kotlinx.android.synthetic.main.viewpager_worker_main_calendar_cell.view.*
import java.time.LocalDate


class WorkerMainActivity : JWBaseActivity() {
    private lateinit var thisActivity: WorkerMainActivity

    private lateinit var workerViewModel: WorkerViewModel
    private lateinit var binding: ActivityWorkerMainBinding

    private var _id = ""
    private var dateTxt = LocalDate.now()

    private lateinit var tvDate: TextView
    private lateinit var ckbTri: CheckBox
    private lateinit var layIndicator: LinearLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var pickerY: NumberPicker
    private lateinit var pickerM: NumberPicker

    private val mIvDot by lazy { arrayOfNulls<ImageView>(2) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_main)
        thisActivity = this

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_worker_main)
        binding.run {
            workerViewModel = ViewModelProviders.of(thisActivity, WorkerViewModelFactory()).get(WorkerViewModel::class.java)
            lifecycleOwner = thisActivity
            workerMainVm = workerViewModel
        }

        val args = intent
        if (args != null) {
            _id = args.getStringExtra("_id").toString()
            workerViewModel.setEmployeeInfo(_id, dateTxt)
        }

        initView()

    }

    private fun initView() {
        tvDate = binding.workerMainTvDate
        ckbTri = binding.workerMainImgTri
        layIndicator = binding.workerMainLayIndicator
        viewPager = binding.workerMainViewpager
        pickerY = binding.workerMainPickerY
        pickerM = binding.workerMainPickerM

        tvDate.text = dateTxt.toString().substring(0, 7)

        ckbTri.setOnCheckedChangeListener(WorkerMainCheckChangeListener())

        setViewpager()
        setIndicator()      // 뷰페이저 인디케이터 설정
        setNumberPicker()   // 피커설정
    }

    private fun setViewpager() {
        var viewpagerAdapter: BaseRecyclerView.Adapter<String, ViewpagerWorkerMainBinding>? = null

        val liveCalendarList = workerViewModel.liveCalendarList
        workerViewModel.liveVacationList.observe(thisActivity as LifecycleOwner, Observer {
            val liveVacationList = it ?: return@Observer
            viewpagerAdapter = object : BaseRecyclerView.Adapter<String, ViewpagerWorkerMainBinding>(
                    layoutResId = R.layout.viewpager_worker_main,
                    bindingVariableId = BR.workerMainViewpagerVal
            ) {
                override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<ViewpagerWorkerMainBinding>, position: Int) {
                    super.onBindViewHolder(holder, position)
                    // 그리드뷰
                    val gridview = holder.itemView.viewpagerWorkerMain_gridview
                    val calendarAdapter = CalendarAdapter(
                            mContext,
                            R.layout.viewpager_worker_main_calendar_cell,
                            liveCalendarList,
                            liveVacationList
                    )
                    gridview.adapter = calendarAdapter
                    gridview.setOnItemClickListener { parent, view, position, id ->

                        if (view.viewpagerWorkerMain_imgVacation.isVisible) {

                        }

                    }

                    // 리사이클러뷰
                    val recyclerview = holder.itemView.viewpagerWorkerMain_recyclerview
                    recyclerview.setHasFixedSize(true)
                    recyclerview.layoutManager = LinearLayoutManager(thisActivity)
                    recyclerview.adapter = WorkerVacationListAdapter(liveVacationList, thisActivity)
                }

            }

            viewPager.adapter = viewpagerAdapter

        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                for (index in mIvDot.indices) {
                    if (index == position) {
                        mIvDot[index]?.setImageResource(R.drawable.indicator_on)
                    } else {
                        mIvDot[index]?.setImageResource(R.drawable.indicator_nor)
                    }
                }
                if (position == 0) {
                    viewPager.viewpagerWorkerMain_gridview.visibility = View.VISIBLE
                    viewPager.viewpagerWorkerMain_gridviewDay.visibility = View.VISIBLE
                    viewPager.viewpagerWorkerMain_recyclerview.visibility = View.INVISIBLE
                    viewPager.viewpagerWorkerMain_listTitle.visibility = View.INVISIBLE
                    tvDate.text = dateTxt.toString().substring(0, 7)
                } else {
                    viewPager.viewpagerWorkerMain_gridview.visibility = View.INVISIBLE
                    viewPager.viewpagerWorkerMain_gridviewDay.visibility = View.INVISIBLE
                    viewPager.viewpagerWorkerMain_recyclerview.visibility = View.VISIBLE
                    viewPager.viewpagerWorkerMain_listTitle.visibility = View.VISIBLE
                    tvDate.text = dateTxt.toString().substring(0, 4)
                }

            }
        })

    }


    /**
     * 뷰페이저 인디케이터 설정
     */
    private fun setIndicator() {
        layIndicator.removeAllViews()

        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                SizeConverterUtil(this).dp(15F), SizeConverterUtil(this).dp(20F))

        for (index in mIvDot.indices) {
            val ivDot = ImageView(mContext)
            ivDot.setPadding(8, 0, 8, 0)
            ivDot.layoutParams = layoutParams
            if (index == viewPager.currentItem)
                ivDot.setImageResource(R.drawable.indicator_on)
            else
                ivDot.setImageResource(R.drawable.indicator_nor)

            mIvDot[index] = ivDot
            layIndicator.addView(mIvDot[index])
        }
    }

    /**
     * picker 설정
     */
    private fun setNumberPicker() {
        val today = LocalDate.now()
        val year = today.year
        val month = today.monthValue

        pickerY.wrapSelectorWheel = false
        pickerM.wrapSelectorWheel = false

        pickerY.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        pickerM.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        pickerY.minValue = 1900
        pickerM.minValue = 1

        pickerY.maxValue = 2100
        pickerM.maxValue = 12

        pickerY.value = year
        pickerM.value = month


        tvDate.text = year.toString() + "-" + String.format("%02d", month)

        pickerY.setOnValueChangedListener { picker, oldVal, newVal ->
            if (viewPager.currentItem == 0) {
                tvDate.text = newVal.toString() + "-" + String.format("%02d", pickerM.value)
            } else {
                tvDate.text = newVal.toString()
            }
        }

        pickerM.setOnValueChangedListener { picker, oldVal, newVal ->
            tvDate.text = pickerY.value.toString() + "-" + String.format("%02d", newVal)

        }
    }

    /**
     * 체크박스 체크 리스너
     */
    private inner class WorkerMainCheckChangeListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
            if (isChecked) {
                viewPager.visibility = View.INVISIBLE
                pickerY.visibility = View.VISIBLE

                pickerY.value = tvDate.text.toString().substring(0, 4).toInt()
                if (viewPager.currentItem == 0) {
                    pickerM.value = tvDate.text.toString().substring(5, 7).toInt()
                    pickerM.visibility = View.VISIBLE
                }
            } else {
                pickerY.visibility = View.INVISIBLE
                pickerM.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
            }
        }
    }

    fun onClickWorkerMain(view: View) {
        when (view) {
            tvDate -> {
                ckbTri.isChecked = !ckbTri.isChecked
            }
        }

    }
}
