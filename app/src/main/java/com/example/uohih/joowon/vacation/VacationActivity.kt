package com.example.uohih.joowon.vacation

import VacationSearchAdapter
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.StaffData
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.database.DBHelper
import kotlinx.android.synthetic.main.activity_vacation.*
import kotlinx.android.synthetic.main.activity_worker_main.*
import java.io.IOException


class VacationActivity : JWBaseActivity() {

    // 리스트 뷰
    var mainList = arrayListOf<StaffData>()
    var subList = arrayListOf<StaffData>()

//    val mVacationSearchAdapter by lazy {
//        VacationSearchAdapter(this, subList)
//
//    }
    private lateinit var mVacationSearchAdapter: VacationSearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation)

        vacation_edt_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                vacation_ly_lv.visibility = View.VISIBLE
                vacation_sv.visibility = View.GONE

                setSearch(vacation_edt_name.text.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        vacation_listview.onItemClickListener = OnItemClickListener { p0, p1, p2, p3 ->
            vacation_sv.visibility = View.VISIBLE
            vacation_ly_lv.visibility = View.GONE

            val bitmap = subList[p2].picture
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
                exifDegree = exifOrientationToDegrees(exifOrientation)

                Glide.with(this).load(rotate(file, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(vacation_img_people)

            } else {
                vacation_img_people.setImageDrawable(getDrawable(R.drawable.people))
            }

            vacation_tv_name.text = subList[p2].name
            vacation_tv_join.text = subList[p2].joinDate.toString()
            vacation_tv_phone.text = subList[p2].phone
            vacation_tv_vacation.text = subList[p2].use+"/"+subList[p2].total

        }

    }

    override fun onResume() {
        super.onResume()

        setData()
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
        vacation_tv_nothing.visibility = View.VISIBLE
        vacation_listview.visibility = View.GONE

        subList.clear()

        if(!charText.isNullOrEmpty()) {
            for (i in 0 until mainList.size) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환
                if (mainList[i].name.toLowerCase().contains(charText) || mainList[i].phone.toLowerCase().contains(charText)) { // 검색된 데이터를 리스트에 추가한다.
                    subList.add(mainList[i])
                }
            }
            vacation_tv_nothing.visibility = View.GONE
            vacation_listview.visibility = View.VISIBLE
        }
        mVacationSearchAdapter = VacationSearchAdapter(this, subList)
        vacation_listview.adapter = mVacationSearchAdapter
    }

}

