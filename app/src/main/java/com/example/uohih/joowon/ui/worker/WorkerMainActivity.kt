package com.example.uohih.joowon.ui.worker


import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.uohih.joowon.BR
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.database.VacationData
import com.example.uohih.joowon.databinding.ActivityWorkerMainBinding
import com.example.uohih.joowon.databinding.ViewpagerWorkerMainBinding
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.adapter.CalendarAdapter
import com.example.uohih.joowon.util.DateCommonUtil
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import kotlinx.android.synthetic.main.viewpager_worker_main.view.*
import java.time.LocalDate


class WorkerMainActivity : JWBaseActivity() {
    private lateinit var thisActivity: WorkerMainActivity

    private lateinit var workerViewModel: WorkerViewModel
    private lateinit var binding: ActivityWorkerMainBinding

    private var _id = ""
    private var dateTxt = LocalDate.now()

    private lateinit var tvDate: TextView
    private lateinit var layIndicator: LinearLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var name: String
    private lateinit var bitmap: String
    private lateinit var phoneNum: String
    private lateinit var use: String
    private lateinit var total: String
    private lateinit var joinDate: String
    private var no = ""

    private var tempYear = ""

    private var getBundle = Bundle()
    private val dbHelper = DBHelper(this)

    private val todayJson = DateCommonUtil().getToday()

    //    private val workerMainAdapter by lazy { WorkerMainAdapter(supportFragmentManager, vacationList) }
    private val mIvDot by lazy { arrayOfNulls<ImageView>(2) }

    // 리스트 뷰
    private var vacationList = arrayListOf<VacationData>()

    private var localdate = LocalDate.now()


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
//        hideLoading()

    }

    private fun initView() {


        tvDate = binding.workerMainTvDate
        layIndicator = binding.workerMainLayIndicator
        viewPager = binding.workerMainViewpager


        tvDate.text = dateTxt.toString().substring(0, 7)

        setViewpager()


        //현재 날짜 세팅
//        tv_worker_month.text = todayJson.getString("month")
//        edit_worker_year.setText(todayJson.getString("year"))
//        tempYear = edit_worker_year.text.toString()
//
//        //뷰페이저
//        //뷰페이저 화면전환 리스너
//        viewpager_worker.adapter = workerMainAdapter
//        viewpager_worker.addOnPageChangeListener(ViewpagerChangeListener())
//
        setIndicator() //뷰페이저 인디케이터 설정

    }

    private fun setViewpager() {
        var viewpagerAdapter: BaseRecyclerView.Adapter<ArrayList<Int>, ViewpagerWorkerMainBinding>? = null
        lateinit var gridview: GridView
        val liveCalendarList = workerViewModel.liveCalendarList
        workerViewModel.liveVacationList.observe(thisActivity as LifecycleOwner, Observer {
            val liveVacationList = it ?: return@Observer
//            viewPager.adapter

            viewpagerAdapter = object : BaseRecyclerView.Adapter<ArrayList<Int>, ViewpagerWorkerMainBinding>(
                    layoutResId = R.layout.viewpager_worker_main,
                    bindingVariableId = BR.workerMainViewPagerVm
            ) {

                fun afdfdfdfd() {

                }

                override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<ViewpagerWorkerMainBinding>, position: Int) {
                    super.onBindViewHolder(holder, position)
                    gridview = holder.itemView.viewpagerWorkerMain_gridview

                    var calendarAdapter = CalendarAdapter(
                            mContext,
                            R.layout.viewpager_worker_main_calendar_cell,
                            liveCalendarList,
                            liveVacationList
                    )
                    gridview.adapter = calendarAdapter
                }

            }

            viewPager.adapter = viewpagerAdapter

        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//                    previousPosition = viewPager.currentItem
//                }
//                viewPager.post {
//                    viewPagerAdapter.notifyDataSetChanged()
//                }
//            }

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
                    tvDate.text = dateTxt.toString().substring(0, 7)
                } else {
                    viewPager.viewpagerWorkerMain_gridview.visibility = View.INVISIBLE
                    viewPager.viewpagerWorkerMain_gridviewDay.visibility = View.INVISIBLE
                    tvDate.text = dateTxt.toString().substring(0, 4)
                }

            }
        })

//        viewPager.adapter = workerMainAdapter
    }

    /**
     * 뷰페이저 화면전환 리스너
     */
//    private inner class ViewpagerChangeListener : OnPageChangeListener {
//        override fun onPageScrollStateChanged(state: Int) {
//        }
//
//        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        }
//
//        override fun onPageSelected(position: Int) {
//            for (index in 0 until workerMainAdapter.count) {
//                if (index == position) {
//                    mIvDot[index]?.setImageResource(R.drawable.indicator_on)
//                } else {
//                    mIvDot[index]?.setImageResource(R.drawable.indicator_nor)
//                }
//            }
//
//            if (position == 0) {
//                // 캘린더 형
//                btn_worker_calendar.visibility = View.VISIBLE
//                btn_worker_list.visibility = View.GONE
//                tv_worker_month.visibility = View.VISIBLE
//                btn_worker_nextm.visibility = View.VISIBLE
//                btn_worker_backm.visibility = View.VISIBLE
//                val fragment = findFragmentByPosition() as GridWorkerMainFragment
//                fragment.setCalendarView(localdate)
//            } else {
//                // 리스트 형
//                btn_worker_calendar.visibility = View.GONE
//                btn_worker_list.visibility = View.VISIBLE
//                tv_worker_month.visibility = View.INVISIBLE
//                btn_worker_nextm.visibility = View.INVISIBLE
//                btn_worker_backm.visibility = View.INVISIBLE
//                val fragment = findFragmentByPosition() as ListWorkerMainFragment
//                fragment.setNotify()
//            }
//
//            edit_worker_year.setText(tempYear)
//        }
//    }


//    fun onClickWorkerMain(v: View) {
//        edit_worker_year.clearFocus() //년도 포커스 제거
//
//        when (v) {
//            img_worker_people -> {
//                // 프로필 사진 보기
//                val intent = Intent(this, PictureActivity::class.java)
//                intent.putExtra("picture", bitmap)
//                startActivity(intent)
//            }
//            btn_worker_write -> {
//                // 휴가 등록하기
//                val bundle = Bundle()
//                bundle.putString("name", name)
//                bundle.putString("join", joinDate)
//                bundle.putString("phone", phoneNum)
//                bundle.putString("vacation", """$use/$total""")
//                bundle.putString("bitmap", bitmap)
//
//                val cntRemain = (total.toDouble() - use.toDouble())
//                val cntTotal = total
//
//                bundle.putDouble("cntRemain", cntRemain)
//                bundle.putString("cntTotal", cntTotal)
//
//                val intent = Intent(this, BlankActivity::class.java)
//                intent.putExtra("fragmentFlag", "vacation")
//                intent.putExtra("fragmentBundle", bundle)
//
//                startActivity(intent)
//
//            }
//            btn_worker_list -> {
//                btn_worker_calendar.visibility = View.VISIBLE
//                btn_worker_list.visibility = View.GONE
//
//                viewpager_worker.currentItem = 0
//            }
//            btn_worker_calendar -> {
//                btn_worker_calendar.visibility = View.GONE
//                btn_worker_list.visibility = View.VISIBLE
//
//                viewpager_worker.currentItem = 1
//            }
//            btn_worker_call -> {
//                // 전화걸기
//                val intentCall = Intent(Intent.ACTION_DIAL)
//                intentCall.data = Uri.parse("tel:$phoneNum")
//                startActivity(intentCall)
//            }
//            btn_worker_setting -> {
//                // 직원정보 수정하기
//                val intentSetting = Intent(this, WorkerInsertActivity::class.java)
//                intentSetting.putExtra("workerUpdate", "Y")
//                intentSetting.putExtra("workerBundle", getBundle)
//                startActivity(intentSetting)
//            }
//            btn_worker_backm -> {
//                localdate = localdate.minusMonths(1)
//                edit_worker_year.setText(localdate.year.toString())
//                tv_worker_month.text = String.format("%02d", localdate.monthValue)
//            }
//            btn_worker_nextm -> {
//                localdate = localdate.plusMonths(1)
//                edit_worker_year.setText(localdate.year.toString())
//                tv_worker_month.text = String.format("%02d", localdate.monthValue)
//            }
//            btn_worker_search -> {
//                localdate = localdate.withYear(Integer.parseInt(edit_worker_year.text.toString()))
//
//                setVacationData()
//                tempYear = edit_worker_year.text.toString()
//            }
//
//        }
//
//        if (viewpager_worker.currentItem == 0) {
//            val fragment = findFragmentByPosition() as GridWorkerMainFragment
//            fragment.setCalendarView(localdate)
//        } else {
//            val fragment = findFragmentByPosition() as ListWorkerMainFragment
//            fragment.setNotify()
//        }
//
//    }

    /**
     * 뷰페이저 프래그먼트 가져오기
     */
//    private fun findFragmentByPosition(): Fragment? {
//        return supportFragmentManager.findFragmentByTag(
//                workerMainAdapter.getItem(viewpager_worker.currentItem).tag)
//    }

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
     * db에서 데이터 가져온 후 set
     */
//    private fun setVacationData() {
//        val cursor = dbHelper.selectVacation(phoneNum.replace("-", ""), name, edit_worker_year.text.toString())
//        vacationList.clear()
//
//        while (cursor.moveToNext()) {
//            vacationList.add(VacationData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getDouble(5), cursor.getDouble(6)))
//        }
//
//    }

//    private fun setWorkerData() {
//        var cursor = dbHelper.selectWorker(name, phoneNum.replace("-", ""))
//
//        if (!no.isEmpty()) {
//            cursor = dbHelper.selectWorker(no)
//        }
//        cursor.moveToFirst()
//        name = cursor.getString(1)
//        bitmap = cursor.getString(6)
//        phoneNum = (Constants.PHONE_NUM_PATTERN).toRegex().replace(cursor.getString(3).toString(), "$1-$2-$3")
//        use = cursor.getString(4).toString()
//        total = cursor.getString(5).toString()
//        joinDate = (Constants.YYYYMMDD_PATTERN).toRegex().replace(cursor.getInt(2).toString(), "$1-$2-$3")
//        no = cursor.getInt(0).toString()
//
//        tv_worker_name.text = "$name ($joinDate)"
//        tv_worker_phone.text = "P.H $phoneNum"
//        tv_worker_vacation.text = "Vacation $use / $total"
//
//        if (bitmap != "") {
//            val file = BitmapFactory.decodeFile(bitmap)
//            lateinit var exif: ExifInterface
//
//            try {
//                exif = ExifInterface(bitmap)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            val exifOrientation: Int
//            val exifDegree: Int
//
//            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//            exifDegree = exifOrientationToDegrees(exifOrientation)
//
//            Glide.with(this).load(rotate(file, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(img_worker_people)
//
//        }
//
//        getBundle.putString("use", use)
//        getBundle.putString("total", total)
//        getBundle.putString("joinDate", joinDate)
//        getBundle.putString("no", no)
//        getBundle.putString("picture", bitmap)
//    }

}
