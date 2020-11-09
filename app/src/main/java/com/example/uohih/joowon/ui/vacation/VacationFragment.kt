//package com.example.uohih.joowon.ui.vacation
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.library.baseAdapters.BR
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProviders
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.uohih.joowon.R
//import com.example.uohih.joowon.util.LogUtil
//import com.example.uohih.joowon.databinding.FragmentVacationBinding
//import com.example.uohih.joowon.databinding.ListItemVacationBinding
//import com.example.uohih.joowon.model.VacationList
//import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
//import com.example.uohih.joowon.ui.customView.CalendarDialog
//import kotlinx.android.synthetic.main.btn_positive.view.*
//import kotlinx.android.synthetic.main.btn_white.view.*
//import kotlinx.android.synthetic.main.list_item_vacation.view.*
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.time.LocalDate
//import java.util.*
//import kotlin.collections.ArrayList
//
//class VacationFragment : Fragment(), View.OnClickListener {
//    private lateinit var mContext: Context
//    private lateinit var thisFragment: VacationFragment
//
//    private lateinit var vacationViewModel: VacationViewModel
//    private lateinit var binding: FragmentVacationBinding
//
//    private val vacationList = mutableListOf<VacationList>()
//
//    private lateinit var bitmap: String
//
//    private var cntSchedule = 0.0           //사용예정휴가일수
//    private var cntRemain = 0.0             //남은휴가일수
//    private var cntTotal = ""               //총휴가일수
//
//
//    private var _id = ""
//
//    private lateinit var btnCalendar: ImageButton
//    private lateinit var tvDate: TextView
//    private lateinit var tvHalf: TextView
//    private lateinit var ckbHalf: CheckBox
//    private lateinit var edtContent: EditText
//    private lateinit var btnRegisterV: Button
//    private lateinit var btnRegister: Button
//    private lateinit var recyclerView: RecyclerView
//
//    //    private val customDialog by lazy {
////        CustomDialog(mContext)
////    }
//    companion object {
//        @JvmStatic
//        fun newInstance(): VacationFragment {
//            return VacationFragment().apply {
//                arguments = Bundle().apply {
//                }
//            }
//        }
//
//        fun newInstance(bundle: Bundle): VacationFragment {
//            return VacationFragment().apply {
//                arguments = Bundle().apply {
//                    _id = bundle.getString("_id", "")
//                }
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mContext = this.requireActivity()
//        thisFragment = this
//
//
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
////        val root = inflater.inflate(R.layout.fragment_vacation, container, false)
//
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacation, container, false)
//        binding.run {
//            vacationViewModel = ViewModelProvider(thisFragment, VacationViewModelFactory()).get(VacationViewModel::class.java)
//            lifecycleOwner = thisFragment
//            vacationEVm = vacationViewModel
//        }
//
//        val root = binding.root
//
//        return root
//    }
//
//    // 뷰 생성이 완료되면 호출되는 메소드
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        // 휴가 시작 기간
////        vacation_tv_startD.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(todayJson, "$1-$2-$3")
////        vacation_btn_startC.setOnClickListener(this)
////
////        // 휴가 마지막 기간
////        vacation_tv_endD.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(todayJson, "$1-$2-$3")
////        vacation_btn_endC.setOnClickListener(this)
////
////        cntSchedule = (calDateBetweenAandB(vacation_tv_startD.text.toString(), vacation_tv_endD.text.toString()))
////        vacation_tv_use_vc.text = String.format(getString(R.string.vacation_use_vacation), cntSchedule)
////
////        // 하단 등록 버튼
////        vacation_btn_bottom.setOnClickListener(this)
//        initView()
//    }
//
//    fun initView() {
//        if (_id.isNotEmpty()) {
//            vacationViewModel.setEmployeeInfo(_id)
//        }
//
//        btnCalendar = binding.vacationBtnCalendar
//        tvDate = binding.vacationTvDate
//        tvHalf = binding.vacationTvHalf
//        ckbHalf = binding.vacationCkbHalf
//        edtContent = binding.vacationEdtContent
//        btnRegisterV = binding.vacationBtnRegisterV.btnWhite
//        btnRegister = binding.vacationBtnRegister.btnPositive
//        recyclerView = binding.vacationRecyclerViewE
//
//        btnRegisterV.text = getString(R.string.vacation_add)
//        btnRegister.text = getString(R.string.vacation_title)
//        tvDate.text = LocalDate.now().toString() + " ~ " + LocalDate.now().toString()
//
//        btnCalendar.setOnClickListener(this)
//        tvDate.setOnClickListener(this)
//        tvHalf.setOnClickListener(this)
//        btnRegisterV.setOnClickListener(this)
//        btnRegister.setOnClickListener(this)
//
//        setRecyclerView()
//
//
////        if (bitmap != "") {
////            val file = BitmapFactory.decodeFile(bitmap)
////            lateinit var exif: ExifInterface
////
////            try {
////                exif = ExifInterface(bitmap)
////            } catch (e: IOException) {
////                e.printStackTrace()
////            }
////
////            val exifOrientation: Int
////            val exifDegree: Int
////
////            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
////            exifDegree = baseActivity.exifOrientationToDegrees(exifOrientation)
////
//////            Glide.with(mContext).load(baseActivity.rotate(file, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(vacation_img_people)
////
////        } else {
////            vacation_img_people.setImageDrawable(mContext.getDrawable(R.drawable.people))
////        }
//
////        vacation_tv_name.text = name
////        vacation_tv_join.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(join, "$1-$2-$3")
////        vacation_tv_phone.text = (Constants.PHONE_NUM_PATTERN).toRegex().replace(phone, "$1-$2-$3")
////        vacation_tv_vacation.text = vacation
//    }
//
//    private fun setRecyclerView() {
//        vacationViewModel.setInitVacationList()
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(mContext)
//        recyclerView.adapter = object : BaseRecyclerView.Adapter<VacationList, ListItemVacationBinding>(
//                layoutResId = R.layout.list_item_vacation,
//                bindingVariableId = BR.vacationRItem
//        ) {
//            override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<ListItemVacationBinding>, position: Int) {
//                super.onBindViewHolder(holder, position)
//
//                holder.itemView.item_vacationR_delete.setOnClickListener {
//                    Toast.makeText(mContext, "삭제.", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
//    }
//
//
//    override fun onClick(v: View) {
//        when (v) {
//            btnCalendar, tvDate -> {
//                showCalendarDialog(LocalDate.now().toString())
//            }
//            tvHalf ->{
//                ckbHalf.isChecked = !ckbHalf.isChecked
//            }
//            btnRegisterV -> {
//                // 휴가 리스트에 추가
//                addVacationList()
//            }
//            btnRegister -> {
//
//            }
////
////            R.id.vacation_btn_startC -> {
////                showCalendarDialog(vacation_tv_startD.text.toString(), vacation_tv_startD)
////            }
////            R.id.vacation_btn_endC -> {
////                showCalendarDialog(vacation_tv_endD.text.toString(), vacation_tv_endD)
////            }
////            R.id.vacation_btn_bottom -> {
////                if (validation()) {
////                    //todo
////                    LogUtil.e("True")
////                    setVacationRegister()
////                }
////            }
//
//        }
//    }
//
//    /**
//     * 휴가리스트 추가
//     */
//    private fun addVacationList() {
//        val dateArr = tvDate.text.toString().split("~")
//        val weekdayList = calDateWeekday(dateArr[0], dateArr[1])
//
//        val isHalf = if(ckbHalf.isChecked) "0.5" else "1.0"
//
//        for (i in 0 until weekdayList.size) {
//            for(j in 0 until vacationList.size){
//                if(vacationList[j].vacation_date == weekdayList[i]){
//                    vacationList.removeAt(j)
//                }
//            }
//            vacationList.add(VacationList(weekdayList[i], edtContent.text.toString(), isHalf))
//        }
//
//        vacationViewModel.addVacationList(vacationList)
//
//
//        var totalVacationCnt = 0f
//        for(i in 0 until vacationList.size){
//            if(vacationList[i].vacation_cnt != null) {
//                totalVacationCnt += vacationList[i].vacation_cnt!!.toFloat()
//            }
//        }
//
//
//        btnRegister.text = getString(R.string.vacation_title)
//
//    }
//
//    /**
//     * 검증
//     */
//    private fun validation(): Boolean {
////        if (vacationList.size == 0) {
////            customDialog.setBottomDialog(
////                    resources.getString(R.string.vacation_dialog_msg4),
////                    resources.getString(R.string.btnConfirm), null)
////            customDialog.show()
////            return false
////        }
////        if (vacation_edt_content.text.isNullOrEmpty())
////            vacation_edt_content.setText(getString(R.string.vacation_content2))
//
//        // 휴가 일수 확인
//        return cntRemain >= cntSchedule
//    }
//
//
//    /**
//     * 날짜 사이 평일 수 구하기
//     */
//    private fun calDateWeekday(date1: String, date2: String): ArrayList<String> {
//        val weekdayList = arrayListOf<String>()
//        try {
//            val sdf = SimpleDateFormat("yyyy-MM-dd")
//            val start = Calendar.getInstance()
//            val end = Calendar.getInstance()
//            var workingDays = 0.0
//
//            start.time = sdf.parse(date1)
//            end.time = sdf.parse(date2)
//
//            while (!start.after(end)) {
//                val day = start.get(Calendar.DAY_OF_WEEK)
//                if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
//                    workingDays++
//                    weekdayList.add(sdf.format(start.time))
//                }
//                start.add(Calendar.DATE, 1)
//
//            }
//
//            LogUtil.e(weekdayList, workingDays)
//
//////            vacation_listview.adapter = mVacationAdapter.apply {
//////                setVacationAdapterListener(object : VacationAdapter.VacationAdapterListener {
//////                    override fun getItemHeight(height: Int) {
//////                        val params = vacation_listview?.layoutParams
//////                        params?.height = height
//////                        vacation_listview?.layoutParams = params
//////                        vacation_listview?.requestLayout()
//////                    }
//////
//////                    override fun getVacationCnt(mCheckBoxList: ArrayList<Boolean>, cnt: Double) {
//////                        vacation_tv_use_vc.text = String.format(getString(R.string.vacation_use_vacation), cnt)
//////                        workingDays = cnt
//////                        checkBoxList = mCheckBoxList
//////                    }
//////                })
//////            }
////
////            checkBoxList = mVacationAdapter.getCheckBoxList()
//
//
////            setListViewHeightBasedOnChildren(vacation_listview)
//
//            return weekdayList
//
//        } catch (e: ParseException) {
//            LogUtil.e(e)
//            return weekdayList
//        }
//    }
//
//
//    /**
//     * 캘린더 다이얼로그
//     */
//    private fun showCalendarDialog(date: String) {
//        val calendarDialog = CalendarDialog(mContext).apply {
//            setBottomDialog(
//                    date,
//                    null,
//                    object : CalendarDialog.ConfirmBtnClickListener {
//                        override fun onConfirmClick(date: ArrayList<LocalDate>) {
//                            if (date.size > 0) {
//                                val strDate = date[0].toString() + " ~ " + date[date.size - 1].toString()
//                                tvDate.text = strDate
//                            }
//                        }
//                    },
//                    isFutureSelect = true,
//                    isSelectedMulti = false,
//                    isSelectedRang = true)
//        }
//
//        calendarDialog.show()
//
//    }
//
//
//    /**
//     * 캘린더 다이얼로그
//     */
//    private fun showCalendarDialog(date: String, mTv: TextView) {
////        var calendarDialog = CalendarDialog(mContext)
////        calendarDialog = calendarDialog.createDialogCalendar(mContext, date, true)!!
////        calendarDialog.show()
////        calendarDialog.setOnDismissListener {
////            if (mTv.id == R.id.vacation_tv_endD) {
////                val startD = vacation_tv_startD.text.toString().replace("-", "").toInt()
////                if (startD > baseApplication.getSelectDate().toInt()) {
////                    customDialog.setBottomDialog(mContext, resources.getString(R.string.vacation_dialog_msg3), resources.getString(R.string.btnConfirm), null)
////                    return@setOnDismissListener
////                }
////            }
////            mTv.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(baseApplication.getSelectDate(), "$1-$2-$3")
////            cntSchedule = calDateBetweenAandB(vacation_tv_startD.text.toString(), vacation_tv_endD.text.toString())
////            vacation_tv_use_vc.text = String.format(getString(R.string.vacation_use_vacation), cntSchedule)
////        }
//    }
//
////    private fun setVacationRegister() {
//////        val name = vacation_tv_name.text.toString()
////        val phone = vacation_tv_phone.text.toString().replace("-", "")
////        val content = vacation_edt_content.text.toString()
////        val joinDate = vacation_tv_join.text.toString().replace("-", "")
////
////        val cursor = dbHelper.selectWorker(name, phone.replace("-", ""))
////        cursor.moveToFirst()
////        val bitmap = cursor.getString(6)
////        var use = cursor.getString(4).toString()
////        val no = cursor.getInt(0).toString()
////
////        for (i in 0 until vacationList.size) {
////            val date = vacationList[i].replace("-", "")
////            val cntUse by lazy {
////                if (checkBoxList[i]) "0.5"
////                else "1.0"
////            }
////
////            use = (use.toDouble() + cntUse.toDouble()).toString()
////
////            val cursor = dbHelper.selectVacation(phone, name, date)
////
////            if (cursor.count > 0) {
////                customDialog.setBottomDialog(
////                        getString(R.string.vacation_dialog_msg2),
////                        getString(R.string.btnConfirm), null)
////                customDialog.show()
////                return
////            }
////
////            dbHelper.insert(dbHelper.tableNameVacationJW, "name", "phone", "date", "content", "use", "total",
////                    name, phone, date, content, cntUse, cntTotal)
////
////            dbHelper.update(dbHelper.tableNameWorkerJW, name, joinDate, phone, use, cntTotal, bitmap, no)
////        }
////
////        customDialog.setBottomDialog(
////                getString(R.string.vacation_dialog_msg),
////                getString(R.string.btnConfirm),
////                View.OnClickListener {
////
////                    if (mContext is VacationActivity) {
//////                        (mContext as VacationActivity).clearEditName()
////                    } else {
////                        (mContext as Activity).finish()
////                    }
////                    customDialog.dismiss()
////                })
////        customDialog.show()
////    }
//
//
//    private fun vacationExistence() {
//
//    }
//
//    /**
//     * 리스트뷰 높이 설정
//     */
//    private fun setListViewHeightBasedOnChildren(listView: ListView) {
//        val listAdapter = listView.adapter ?: return // pre-condition
//
//        var totalHeight = 0
//        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
//        val listCnt by lazy {
//            if (listAdapter.count > 3) {
//                listAdapter.count - 1
//            } else listAdapter.count
//        }
//
//        for (i in 0 until listCnt) {
//            val listItem = listAdapter.getView(i, null, listView)
//            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
//            totalHeight += listItem.measuredHeight
//        }
//
//        val params = listView.layoutParams
//        params.height = totalHeight
//        listView.layoutParams = params
//        listView.requestLayout()
//    }
//}