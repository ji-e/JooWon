package com.example.uohih.joowon.ui.worker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.uohih.joowon.BR
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivityWorkerMainBinding
import com.example.uohih.joowon.databinding.ViewpagerWorkerMainBinding
import com.example.uohih.joowon.model.CalendarDayInfo
import com.example.uohih.joowon.model.VacationList
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.adapter.CalendarAdapter
import com.example.uohih.joowon.ui.adapter.WorkerVacationListAdapter
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.vacation.VacationDeleteActivity
import com.example.uohih.joowon.ui.vacation.VacationRegisterActivity
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.viewpager_worker_main.view.*
import kotlinx.android.synthetic.main.viewpager_worker_main_calendar_cell.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate


class WorkerMainActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private val workerViewModel: WorkerViewModel by viewModel()
    private lateinit var binding: ActivityWorkerMainBinding

    private var _id = ""
    private var localDate = LocalDate.now()
    private var preYear = localDate.year.toString()

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

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_worker_main)
        binding.run {
            lifecycleOwner = thisActivity
            workerMainVm = workerViewModel
        }

        val args = intent
        if (args != null) {
            _id = args.getStringExtra("_id").toString()
            workerViewModel.setEmployeeInfo(_id)
            workerViewModel.setInitWorkerMainActivity()
        }

        initView()

    }

    override fun onResume() {
        super.onResume()
        localDate = LocalDate.now()
        preYear = localDate.year.toString()

        if (_id.isNotEmpty()) {
            workerViewModel.setCalendarList(localDate)
            findVacation()
            setNumberPicker()   // 피커설정
        }


    }

    private fun initView() {
        tvDate = binding.workerMainTvDate
        ckbTri = binding.workerMainImgTri
        layIndicator = binding.workerMainLayIndicator
        viewPager = binding.workerMainViewpager
        pickerY = binding.workerMainPickerY
        pickerM = binding.workerMainPickerM

        tvDate.text = localDate.toString().substring(0, 7)

        ckbTri.setOnCheckedChangeListener(WorkerMainCheckChangeListener())

        setViewpager()
        setIndicator()      // 뷰페이저 인디케이터 설정
        setObserve()
    }

    private fun setObserve() {

        with(workerViewModel) {
            isNetworkErr.observe(thisActivity, Observer {
                if (it) {
                    showNetworkErrDialog(mContext)
                }
            })

            isLoading.observe(thisActivity, Observer {
                when {
                    it -> showLoading()
                    else -> hideLoading()
                }
            })

            jw4003Data.observe(thisActivity, Observer { jw4003Data ->
                if ("ZZZZ" == jw4003Data.errCode) {
                    showSessionOutDialog(thisActivity)
                    return@Observer
                }

                if ("failure" == jw4003Data.result) {
                    val customDialog = CustomDialog(mContext).apply {
                        setBottomDialog(
                                strContent = jw4003Data.msg.toString(),
                                strYes = getString(R.string.btnConfirm),
                                onYesListener = View.OnClickListener {
                                    dismiss()
                                    finish()
                                })
                    }
                    customDialog.show()

                    return@Observer
                }


                val useVacation = jw4003Data.resbody?.vacationList
                val useVacationSize = jw4003Data.resbody?.vacationList?.size ?: 0
                var useVacationCnt = 0f
                for (i in 0 until useVacationSize) {
                    useVacationCnt += (useVacation?.get(i)?.vacation_cnt)?.toFloat() ?: 0f
                }

                val bundle = bundleOf("use_vacation_cnt" to useVacationCnt.toString())
                UICommonUtil.setEmployeeInfo(_id, bundle, useVacation)
                workerViewModel.setEmployeeInfo(_id)

            })

        }
    }

    private fun setViewpager() {
        val viewpagerAdapter = object : BaseRecyclerView.Adapter<String, ViewpagerWorkerMainBinding>(
                layoutResId = R.layout.viewpager_worker_main,
                bindingVariableId = BR.workerMainViewpagerVal
        ) {

            override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<ViewpagerWorkerMainBinding>, position: Int) {
                super.onBindViewHolder(holder, position)
                var liveCalendarList = workerViewModel.liveCalendarList
                var liveVacationList = workerViewModel.liveVacationList.value
                // 그리드뷰
                val calendarAdapter =
                        CalendarAdapter(
                                mContext,
                                R.layout.viewpager_worker_main_calendar_cell,
                                liveCalendarList,
                                liveVacationList)
                val gridview = holder.itemView.viewpagerWorkerMain_gridview
                gridview.adapter = calendarAdapter
                gridview.setOnItemClickListener { parent, view, position, id ->
                    if ((view.viewpagerWorkerMain_imgVacation).visibility == View.VISIBLE && liveVacationList != null) {
                        // 휴가가 등록되어있는 날짜를 클릭한 경우
                        setVacationListClick(true, liveCalendarList, liveVacationList, position)
                    } else {
                        // 휴가가 미등록되어있는 날짜를 클릭한 경우
                        setVacationListClick(false, liveCalendarList, null, position)
                    }

                }

                // 리사이클러뷰
                val workerVacationListAdapter = WorkerVacationListAdapter(liveVacationList, thisActivity)
                val recyclerview = holder.itemView.viewpagerWorkerMain_recyclerview
                recyclerview.setHasFixedSize(true)
                recyclerview.layoutManager = LinearLayoutManager(thisActivity)
                recyclerview.adapter = workerVacationListAdapter

                workerVacationListAdapter.setItemClickListener(object : WorkerVacationListAdapter.ItemClickListener {
                    override fun onItemClick(point: Int) {
                        setVacationListClick(true, liveCalendarList, liveVacationList, position)
                    }

                })

                with(workerViewModel) {
                    liveCalendarInfo.observe(thisActivity as LifecycleOwner, Observer {
                        liveCalendarList = this.liveCalendarList
                        liveVacationList = this.liveVacationList.value

                        workerVacationListAdapter.setVacationList(liveVacationList)
                        calendarAdapter.setVacationList(liveVacationList, liveCalendarList)
                    })
                }
            }
        }

        viewPager.adapter = viewpagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                for (index in mIvDot.indices) {
                    if (index == position) {
                        mIvDot[index]?.setImageResource(R.drawable.indicator_on)
                    } else {
                        mIvDot[index]?.setImageResource(R.drawable.indicator_nor)
                    }
                }

                val mVGridView = viewPager.viewpagerWorkerMain_gridview
                val mVDay = viewPager.viewpagerWorkerMain_gridviewDay
                val mVRecyclerView = viewPager.viewpagerWorkerMain_recyclerview
                val mVRecyclerViewTitle = viewPager.viewpagerWorkerMain_listTitle

                if (position == 0) {
                    mVGridView.visibility = View.VISIBLE
                    mVDay.visibility = View.VISIBLE
                    mVRecyclerView.visibility = View.INVISIBLE
                    mVRecyclerViewTitle.visibility = View.INVISIBLE
                    tvDate.text = localDate.toString().substring(0, 7)
                } else {
                    mVGridView.visibility = View.INVISIBLE
                    mVDay.visibility = View.INVISIBLE
                    mVRecyclerView.visibility = View.VISIBLE
                    mVRecyclerViewTitle.visibility = View.VISIBLE
                    tvDate.text = localDate.toString().substring(0, 4)
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
        val today = localDate
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
            localDate = LocalDate.of(newVal, pickerM.value, 1)
        }

        pickerM.setOnValueChangedListener { picker, oldVal, newVal ->
            tvDate.text = pickerY.value.toString() + "-" + String.format("%02d", newVal)
            localDate = LocalDate.of(pickerY.value, newVal, 1)
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

                pickerY.value = localDate.year
                if (viewPager.currentItem == 0) {
                    pickerM.value = localDate.monthValue
                    pickerM.visibility = View.VISIBLE
                }
            } else {
                pickerY.visibility = View.INVISIBLE
                pickerM.visibility = View.GONE
                viewPager.visibility = View.VISIBLE

                if (preYear != pickerY.value.toString()) {
                    // 년도를 설정한 경우
                    preYear = pickerY.value.toString()
                    findVacation()
                } else {
                    workerViewModel.setCalendarList(localDate)
                }
            }
        }
    }


    /**
     * 휴가리스트 클릭
     */
    private fun setVacationListClick(isVacation: Boolean, liveCalendarList: ArrayList<CalendarDayInfo>, liveVacationList: ArrayList<VacationList>?, position: Int) {
        if (isVacation) {
            val vacationInfo = UICommonUtil.getVacationInfo(liveCalendarList[position].getDate().toString(), liveVacationList!!)
                    ?: liveVacationList[position]
            val intent = Intent(thisActivity, VacationDeleteActivity::class.java).apply {
                putExtra("vacation_cnt", vacationInfo.vacation_cnt)
                putExtra("vacation_content", vacationInfo.vacation_content)
                putExtra("vacation_date", vacationInfo.vacation_date)
                putExtra("_id", _id)
                putExtra("vacation_id", vacationInfo._id)
            }
            startActivity(intent)
        } else {
            val intent = Intent(thisActivity, VacationRegisterActivity::class.java).apply {
                putExtra("_id", _id)
                putExtra("date", liveCalendarList[position].getDate().toString())
            }
            startActivity(intent)
        }
    }

    /**
     * 휴가리스트 조회
     * jw4003
     */
    private fun findVacation() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW4003)
        jsonObject.addProperty("vacation_id", _id)
        jsonObject.addProperty("year", preYear)
        workerViewModel.findVacation(localDate, jsonObject)

    }

    private val requestActivity = registerForActivityResult(StartActivityForResult()) { activityResult ->
        if (activityResult.data?.hasExtra("WORKER_DELETE") == true) {
            if ("Y" == activityResult.data?.getStringExtra("WORKER_DELETE").toString()) {
                finish()
            }
        }
    }

    fun onClickWorkerMain(view: View) {
        when (view) {
            tvDate -> {
                // 날짜 클릭
                ckbTri.isChecked = !ckbTri.isChecked
            }
            binding.workerMainBtnWrite -> {
                // 휴가등록버튼 클릭
                val intent = Intent(thisActivity, VacationRegisterActivity::class.java)
                intent.putExtra("_id", _id)
                startActivity(intent)
            }

            binding.workerMainBtnCalendar -> {
                // 캘린더버튼 클릭
                binding.workerMainBtnCalendar.visibility = View.GONE
                binding.workerMainBtnList.visibility = View.VISIBLE
                viewPager.currentItem = 1
            }
            binding.workerMainBtnList -> {
                // 리스트버튼 클릭
                binding.workerMainBtnCalendar.visibility = View.VISIBLE
                binding.workerMainBtnList.visibility = View.GONE
                viewPager.currentItem = 0
            }
            binding.workerMainBtnCall -> {
                // 전화버튼 클릭
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + workerViewModel.liveEmployeeInfo.value?.phone_number?.replace("-", ""))))
            }
            binding.workerMainBtnSetting -> {
                // 세팅버튼 클릭
                val intent = Intent(thisActivity, WorkerInsertActivity::class.java)
                intent.putExtra("_id", _id)
                requestActivity.launch(intent)
            }
        }

    }
}
