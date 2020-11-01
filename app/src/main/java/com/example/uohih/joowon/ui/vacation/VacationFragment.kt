package com.example.uohih.joowon.ui.vacation

import com.example.uohih.joowon.ui.adapter.VacationAdapter
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.ui.customView.CalendarDialog
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.util.DateCommonUtil
import kotlinx.android.synthetic.main.fragment_vacation.*
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class VacationFragment : Fragment(), View.OnClickListener {
    private lateinit var mContext: Context

    private val dbHelper by lazy { DBHelper(mContext) }

    private val baseActivity = JWBaseActivity()
    private val baseApplication = JWBaseApplication()
    private lateinit var thisFragment: VacationFragment

    private val todayJson = DateCommonUtil().getToday().get("yyyymmdd").toString()

    private val vacationList = arrayListOf<String>()
    private var checkBoxList = arrayListOf<Boolean>(false)

    private lateinit var bitmap: String
    private lateinit var name: String
    private lateinit var join: String
    private lateinit var phone: String
    private lateinit var vacation: String

    private var cntSchedule = 0.0           //사용예정휴가일수
    private var cntRemain = 0.0             //남은휴가일수
    private var cntTotal = ""               //총휴가일수


    private val customDialog by lazy {
        CustomDialog(mContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this.activity!!
        thisFragment = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_vacation, container, false)
        return root
    }

    // 뷰 생성이 완료되면 호출되는 메소드

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 휴가 시작 기간
        vacation_tv_startD.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(todayJson, "$1-$2-$3")
        vacation_btn_startC.setOnClickListener(this)

        // 휴가 마지막 기간
        vacation_tv_endD.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(todayJson, "$1-$2-$3")
        vacation_btn_endC.setOnClickListener(this)

        cntSchedule = (calDateBetweenAandB(vacation_tv_startD.text.toString(), vacation_tv_endD.text.toString()))
        vacation_tv_use_vc.text = String.format(getString(R.string.vacation_use_vacation), cntSchedule)

        // 하단 등록 버튼
        vacation_btn_bottom.setOnClickListener(this)
        initView()
    }

    fun initView() {
        if (bitmap != "") {
            val file = BitmapFactory.decodeFile(bitmap)
            lateinit var exif: ExifInterface

            try {
                exif = ExifInterface(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val exifOrientation: Int
            val exifDegree: Int

            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            exifDegree = baseActivity.exifOrientationToDegrees(exifOrientation)

            Glide.with(mContext).load(baseActivity.rotate(file, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(vacation_img_people)

        } else {
            vacation_img_people.setImageDrawable(mContext.getDrawable(R.drawable.people))
        }

        vacation_tv_name.text = name
        vacation_tv_join.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(join, "$1-$2-$3")
        vacation_tv_phone.text = (Constants.PHONE_NUM_PATTERN).toRegex().replace(phone, "$1-$2-$3")
        vacation_tv_vacation.text = vacation
    }

    companion object {
        @JvmStatic
        fun newInstance(): VacationFragment {
            return VacationFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }

        fun newInstance(bundle: Bundle): VacationFragment {
            return VacationFragment().apply {
                arguments = Bundle().apply {
                    cntRemain = bundle.getDouble("cntRemain", 0.0)
                    cntTotal = bundle.getString("cntTotal", "")
                    name = bundle.getString("name", "")
                    join = bundle.getString("join", "")
                    phone = bundle.getString("phone", "")
                    vacation = bundle.getString("vacation", "")
                    bitmap = bundle.getString("bitmap", "")
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.vacation_btn_startC -> {
                showCalendarDialog(vacation_tv_startD.text.toString(), vacation_tv_startD)
            }
            R.id.vacation_btn_endC -> {
                showCalendarDialog(vacation_tv_endD.text.toString(), vacation_tv_endD)
            }
            R.id.vacation_btn_bottom -> {
                if (validation()) {
                    //todo
                    LogUtil.e("True")
                    setVacationRegister()
                }
            }

        }
    }

    /**
     * 검증
     */
    private fun validation(): Boolean {
        if (vacationList.size == 0) {
            customDialog.setBottomDialog(
                    resources.getString(R.string.vacation_dialog_msg4),
                    resources.getString(R.string.btnConfirm), null)
            customDialog.show()
            return false
        }
        if (vacation_edt_content.text.isNullOrEmpty())
            vacation_edt_content.setText(getString(R.string.vacation_content2))

        // 휴가 일수 확인
        return cntRemain >= cntSchedule
    }


    /**
     * 날짜 사이 평일 수 구하기
     */
    private fun calDateBetweenAandB(date1: String, date2: String): Double {
        try {
            vacationList.clear()
            val mVacationAdapter by lazy { VacationAdapter(mContext, vacationList) }
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            var workingDays = 0.0

            start.time = sdf.parse(date1)
            end.time = sdf.parse(date2)

            while (!start.after(end)) {
                val day = start.get(Calendar.DAY_OF_WEEK)
                if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
                    workingDays++
                    vacationList.add(sdf.format(start.time))
                }
                start.add(Calendar.DATE, 1)

            }

            vacation_listview.adapter = mVacationAdapter.apply {
                setVacationAdapterListener(object : VacationAdapter.VacationAdapterListener {
                    override fun getItemHeight(height: Int) {
                        val params = vacation_listview?.layoutParams
                        params?.height = height
                        vacation_listview?.layoutParams = params
                        vacation_listview?.requestLayout()
                    }

                    override fun getVacationCnt(mCheckBoxList: ArrayList<Boolean>, cnt: Double) {
                        vacation_tv_use_vc.text = String.format(getString(R.string.vacation_use_vacation), cnt)
                        workingDays = cnt
                        checkBoxList = mCheckBoxList
                    }
                })
            }

            checkBoxList = mVacationAdapter.getCheckBoxList()


//            setListViewHeightBasedOnChildren(vacation_listview)

            return workingDays

        } catch (e: ParseException) {
            LogUtil.e(e)
            return 0.0
        }
    }

    /**
     * 캘린더 다이얼로그
     */
    private fun showCalendarDialog(date: String, mTv: TextView) {
//        var calendarDialog = CalendarDialog(mContext)
//        calendarDialog = calendarDialog.createDialogCalendar(mContext, date, true)!!
//        calendarDialog.show()
//        calendarDialog.setOnDismissListener {
//            if (mTv.id == R.id.vacation_tv_endD) {
//                val startD = vacation_tv_startD.text.toString().replace("-", "").toInt()
//                if (startD > baseApplication.getSelectDate().toInt()) {
//                    customDialog.setBottomDialog(mContext, resources.getString(R.string.vacation_dialog_msg3), resources.getString(R.string.btnConfirm), null)
//                    return@setOnDismissListener
//                }
//            }
//            mTv.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(baseApplication.getSelectDate(), "$1-$2-$3")
//            cntSchedule = calDateBetweenAandB(vacation_tv_startD.text.toString(), vacation_tv_endD.text.toString())
//            vacation_tv_use_vc.text = String.format(getString(R.string.vacation_use_vacation), cntSchedule)
//        }
    }

    private fun setVacationRegister() {
//        val name = vacation_tv_name.text.toString()
        val phone = vacation_tv_phone.text.toString().replace("-", "")
        val content = vacation_edt_content.text.toString()
        val joinDate = vacation_tv_join.text.toString().replace("-", "")

        val cursor = dbHelper.selectWorker(name, phone.replace("-", ""))
        cursor.moveToFirst()
        val bitmap = cursor.getString(6)
        var use = cursor.getString(4).toString()
        val no = cursor.getInt(0).toString()

        for (i in 0 until vacationList.size) {
            val date = vacationList[i].replace("-", "")
            val cntUse by lazy {
                if (checkBoxList[i]) "0.5"
                else "1.0"
            }

            use = (use.toDouble() + cntUse.toDouble()).toString()

            val cursor = dbHelper.selectVacation(phone, name, date)

            if (cursor.count > 0) {
                customDialog.setBottomDialog(
                        getString(R.string.vacation_dialog_msg2),
                        getString(R.string.btnConfirm), null)
                customDialog.show()
                return
            }

            dbHelper.insert(dbHelper.tableNameVacationJW, "name", "phone", "date", "content", "use", "total",
                    name, phone, date, content, cntUse, cntTotal)

            dbHelper.update(dbHelper.tableNameWorkerJW, name, joinDate, phone, use, cntTotal, bitmap, no)
        }

        customDialog.setBottomDialog(
                getString(R.string.vacation_dialog_msg),
                getString(R.string.btnConfirm),
                View.OnClickListener {

                    if (mContext is VacationActivity) {
//                        (mContext as VacationActivity).clearEditName()
                    } else {
                        (mContext as Activity).finish()
                    }
                    customDialog.dismiss()
                })
        customDialog.show()
    }


    private fun vacationExistence() {

    }
    /**
     * 리스트뷰 높이 설정
     */
    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return // pre-condition

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        val listCnt by lazy {
            if (listAdapter.count > 3) {
                listAdapter.count - 1
            } else listAdapter.count
        }

        for (i in 0 until listCnt) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight
        listView.layoutParams = params
        listView.requestLayout()
    }
}