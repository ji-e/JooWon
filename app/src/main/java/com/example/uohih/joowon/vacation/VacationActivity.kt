package com.example.uohih.joowon.vacation

import VacationSearchAdapter
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.StaffData
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.view.CustomDialog
import kotlinx.android.synthetic.main.activity_vacation.*


class VacationActivity : JWBaseActivity(), View.OnClickListener {
    private val thisActivity = this
    private val base = JWBaseApplication()
    private val dbHelper = DBHelper(this)

    // 리스트 뷰
    private var mainList = arrayListOf<StaffData>()
    private var subList = arrayListOf<StaffData>()
    private val vacationList = arrayListOf<String>()
    private var checkBoxList = arrayListOf<Boolean>(false)

    private val todayJson = getToday().get("yyyymmdd").toString()

    private lateinit var mVacationSearchAdapter: VacationSearchAdapter

    private val customDialog by lazy {
        CustomDialog(this, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    private var cntSchedule = 0.0           //사용예정휴가일수
    private var cntRemain = 0.0             //남은휴가일수
    private var cntTotal = ""               //총휴가일수



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation)

        initView()

    }

    override fun onResume() {
        super.onResume()

        setData()
    }

    private fun initView() {
        // 검색 에디트
        vacation_edt_name.addTextChangedListener(VacationTextWatcher())

        // 검색 창 delete 버튼
        vacation_btn_delete1.setOnClickListener(this)

        // 검색 버튼
        vacation_btn_search.setOnClickListener(this)

        // 검색결과 리스트뷰
        vacation_listview_search.onItemClickListener = VacationListItemClickListener()

//
//        // 휴가 시작 기간
//        vacation_tv_startD.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(todayJson, "$1-$2-$3")
//        vacation_btn_startC.setOnClickListener(this)
//
//        // 휴가 마지막 기간
//        vacation_tv_endD.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(todayJson, "$1-$2-$3")
//        vacation_btn_endC.setOnClickListener(this)
//
//        cntSchedule = (calDateBetweenAandB(vacation_tv_startD.text.toString(), vacation_tv_endD.text.toString()))
//        vacation_tv_use_vc.text = String.format(getString(R.string.worker_vacation_use_vacation), cntSchedule)
//
//        // 하단 등록 버튼
//        vacation_btn_bottom.setOnClickListener(this)
//        replaceContainerFragment(R.id.vacation_frame_ly, BlankFragment.newInstance())

    }

    inner class VacationTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            vacation_tv_nothing.visibility = View.VISIBLE
            vacation_listview_search.visibility = View.GONE

            if (s.isNotEmpty()) {
                vacation_btn_delete1.visibility = View.VISIBLE
                setSearch(vacation_edt_name.text.toString())

            } else {
                LogUtil.e("FDF")
                subList.clear()
                vacation_btn_delete1.visibility = View.GONE
                vacation_frame_ly.visibility = View.GONE
                vacation_ly_search.visibility = View.VISIBLE
//                vacation_btn_bottom.visibility = View.GONE
            }

            vacation_edt_name.setSelection(vacation_edt_name.text.length)
        }


    }

    inner class VacationListItemClickListener : OnItemClickListener {
        override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            vacation_frame_ly.visibility = View.VISIBLE
            vacation_ly_search.visibility = View.GONE
//            vacation_btn_bottom.visibility = View.VISIBLE

//            val bitmap = subList[p2].picture
//            if (bitmap != "") {
//                val file = BitmapFactory.decodeFile(bitmap)
//                lateinit var exif: ExifInterface
//
//                try {
//                    exif = ExifInterface(bitmap)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//                val exifOrientation: Int
//                val exifDegree: Int
//
//                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//                exifDegree = exifOrientationToDegrees(exifOrientation)
//
//                Glide.with(thisActivity).load(rotate(file, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(vacation_img_people)
//
//            } else {
//                vacation_img_people.setImageDrawable(getDrawable(R.drawable.people))
//            }
//
//            vacation_tv_name.text = subList[p2].name
//            vacation_tv_join.text = subList[p2].joinDate.toString()
//            vacation_tv_phone.text = subList[p2].phone
//            vacation_tv_vacation.text = subList[p2].use + "/" + subList[p2].total

            cntRemain = (subList[position].total.toDouble() - subList[position].use.toDouble())
            cntTotal = subList[position].total

            val imm= getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(vacation_edt_name.windowToken, 0)

            val bundle = Bundle()
            bundle.putString("name", subList[position].name)
            bundle.putString("join", subList[position].joinDate.toString())
            bundle.putString("phone", subList[position].phone)
            bundle.putString("vacation", subList[position].use + "/" + subList[position].total)
            bundle.putString("bitmap", subList[position].picture)

            bundle.putDouble("cntRemain", cntRemain)
            bundle.putString("cntTotal", cntTotal)

            addContainerFragment(R.id.vacation_frame_ly, VacationFragment.newInstance(bundle))

        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.vacation_btn_delete1 ->{
                vacation_edt_name.setText("")
            }
            R.id.vacation_btn_search -> {
                setSearch(vacation_edt_name.text.toString())
            }
//            R.id.vacation_btn_startC -> {
//                showCalendarDialog(vacation_tv_startD.text.toString(), vacation_tv_startD)
//            }
//            R.id.vacation_btn_endC -> {
//                showCalendarDialog(vacation_tv_endD.text.toString(), vacation_tv_endD)
//            }
//            R.id.vacation_btn_bottom -> {
//                if(validation()){
//                    //todo
//                    LogUtil.e("True")
//                    setVacationRegister()
//                }
//            }

        }
    }

    /**
     * db에서 데이터 가져온 후 set
     */
    private fun setData() {
        mainList.clear()
        val dbHelper = DBHelper(this)
        val cursor = dbHelper.selectAll(dbHelper.tableNameWorkerJW)

        while (cursor.moveToNext()) {
            mainList.add(StaffData(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
        }
    }


    /**
     * 검색한 후 set
     * 문자 입력시마다 리스트를 지우고 새로 뿌려줌
     */
    private fun setSearch(charText: String) {
        vacation_frame_ly.visibility = View.GONE
        vacation_ly_search.visibility = View.VISIBLE
//        vacation_btn_bottom.visibility = View.GONE

        subList.clear()
        for (i in 0 until mainList.size) {
            // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환
            if (mainList[i].name.toLowerCase().contains(charText) || mainList[i].phone.toLowerCase().contains(charText)) {
                // 검색된 데이터를 리스트에 추가한다.
                subList.add(mainList[i])
            }
        }

        if (subList.size > 0) {
            vacation_tv_nothing.visibility = View.GONE
            vacation_listview_search.visibility = View.VISIBLE
        }

        mVacationSearchAdapter = VacationSearchAdapter(this, subList)
        vacation_listview_search.adapter = mVacationSearchAdapter
    }


    fun clearEditName() {
        vacation_edt_name.setText("")
    }




}

