package com.example.uohih.joowon.worker


import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.WorkerMainAdapter
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.main.PictureActivity
import kotlinx.android.synthetic.main.activity_worker_main.*
import java.io.IOException
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
    private val instance = Calendar.getInstance()
    private val todayJson = JWBaseActivity().getToday()


    private val workerMainAdapter by lazy { WorkerMainAdapter(supportFragmentManager) }

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
//        setCalendarView(Calendar.getInstance().time, Date())


        //현재 날짜 세팅
        tv_worker_month.text = todayJson.getString("month")
        edit_worker_year.setText(todayJson.getString("year"))


        //뷰페이저
        viewpager_worker.adapter = workerMainAdapter
//        viewpager_worker.addOnAdapterChangeListener();


    }


    fun btnOnClick(v: View) {
        edit_worker_year.clearFocus() //년도 포커스 제거

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
            btn_worker_backm -> {
                instance.add(Calendar.MONTH, -1)
                edit_worker_year.setText(instance.get(Calendar.YEAR).toString())
                tv_worker_month.text = String.format("%02d", instance.get(Calendar.MONTH) + 1)
            }
            btn_worker_nextm -> {
                instance.add(Calendar.MONTH, +1)
                edit_worker_year.setText(instance.get(Calendar.YEAR).toString())
                tv_worker_month.text = String.format("%02d", instance.get(Calendar.MONTH) + 1)
            }
            btn_worker_search -> {
                instance.set(Calendar.YEAR, Integer.parseInt(edit_worker_year.text.toString()))
            }

        }

        if (viewpager_worker.currentItem == 0) {
            val fragment = findFragmentByPosition() as GridWorkerMainFragment
            fragment.setCalendarView(instance.time, Date())
        } else {
            val fragment = findFragmentByPosition() as ListWorkerMainFragment
            //todo
        }


    }

    fun findFragmentByPosition(): Fragment {
        return supportFragmentManager.findFragmentByTag(
                workerMainAdapter.getItem(viewpager_worker.currentItem).tag)!!
    }



}
