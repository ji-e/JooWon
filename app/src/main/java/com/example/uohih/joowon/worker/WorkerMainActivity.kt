package com.example.uohih.joowon.worker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.CalendarAdapter
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.main.PictureActivity
import com.example.uohih.joowon.view.CalendarDayInfo
import kotlinx.android.synthetic.main.activity_worker_insert.*


import kotlinx.android.synthetic.main.activity_worker_main.*
import kotlinx.android.synthetic.main.dialog_calendar.view.*
import kotlinx.android.synthetic.main.grid_worker_main.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WorkerMainActivity : JWBaseActivity() {

    private lateinit var name: String
    private lateinit var bitmap: String
    private lateinit var phoneNum: String
    private lateinit var use: String
    private lateinit var total: String
    private lateinit var joinDate: String
    private var no = ""
    private var getBundle = Bundle()
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_main)


        if (intent.hasExtra("worker")) {
            getBundle = intent.getBundleExtra("worker")
            name = getBundle.getString("name", "")        //이름
            phoneNum = getBundle.getString("phone", "")   //핸드폰번호


        }

    }

    override fun onResume() {
        super.onResume()
        var cursor = dbHelper.selectWorker(name, phoneNum.replace("-", ""))

        if (!no.isEmpty()) {
            cursor = dbHelper.selectWorker(no)
        }
        cursor.moveToFirst()
        name = cursor.getString(1)
        bitmap = cursor.getString(6)
        phoneNum = (Constants.PHONE_NUM_PATTERN).toRegex().replace(cursor.getString(3).toString(), "$1-$2-$3")
        use = cursor.getInt(4).toString()
        total = cursor.getInt(5).toString()
        joinDate = (Constants.YYYYMMDD_PATTERN).toRegex().replace(cursor.getInt(2).toString(), "$1-$2-$3")
        no = cursor.getInt(0).toString()

        tv_worker_name.text = "$name ($joinDate)"
        tv_worker_phone.text = "P.H $phoneNum"
        tv_worker_vacation.text = "Vacation $use / $total"

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

            Glide.with(this).load(rotate(file, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(img_worker_people)

        }

        getBundle.putString("use", use)
        getBundle.putString("total", total)
        getBundle.putString("joinDate", joinDate)
        getBundle.putString("no", no)
        getBundle.putString("picture", bitmap)





        //그리드 캘린더
        val calendarAdapter = CalendarAdapter(this, getCalendar(Calendar.getInstance().time), Date(), R.layout.grid_item_worker_main)
        grid_worker.adapter = calendarAdapter


        //현재 날짜 세팅
        val todayJson = JWBaseActivity().getToday()
        grid_worker_tv_year.text = todayJson.getString("year")
        grid_worker_tv_month.text = todayJson.getString("month")

    }




    fun btnOnClick(v: View) {
        when (v) {
            img_worker_people -> {
                val intent = Intent(this, PictureActivity::class.java)
                intent.putExtra("picture", bitmap)
                startActivity(intent)
            }
            btn_worker_write -> {

            }
            btn_worker_list -> {
                btn_worker_calendar.visibility = View.VISIBLE
                btn_worker_list.visibility = View.GONE
            }
            btn_worker_calendar -> {
                btn_worker_calendar.visibility = View.GONE
                btn_worker_list.visibility = View.VISIBLE
            }
            btn_worker_call -> {
                val intentCall = Intent(Intent.ACTION_DIAL)
                intentCall.data = Uri.parse("tel:$phoneNum")
                startActivity(intentCall)
            }
            btn_worker_setting -> {
                val intentSetting = Intent(this, WorkerInsertActivity::class.java)
                intentSetting.putExtra("workerUpdate", "Y")
                intentSetting.putExtra("workerBundle", getBundle)
                startActivity(intentSetting)
            }

        }
    }
}
