package com.example.uohih.joowon.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.R
import com.example.uohih.joowon.model.CalendarDayInfo
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.customView.CustomLoadingBar
import com.example.uohih.joowon.util.LogUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.util.*
import kotlin.collections.ArrayList


open class JWBaseActivity : AppCompatActivity() {

    val mContext: Context by lazy { this }
    private lateinit var thisActivity: JWBaseActivity
    private val jwBaseViewModel: JWBaseViewModel  by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivity = this
    }

    /**
     * 앱 버전 정보 가져오기
     */
    fun getVersionInfo(): String {
        val info = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
        return info.versionName.toString()

    }

    /**
     * 키패드 데이터 세팅
     */
    fun setKeyPadData(): ArrayList<String> {
        val arrayList = ArrayList<String>()
        val randomList = ArrayList<String>()

        for (i in 0 until 12) {
            when (i) {
                9 -> arrayList.add("왼")
                10 -> arrayList.add("0")
                11 -> arrayList.add("오")
                else -> arrayList.add((i + 1).toString())
            }
        }

        arrayList.shuffle()
        var tempIndex = 0
        var index10 = ""
        for (i in 0 until 12) {

            val temp = arrayList[i]
            if (temp != "왼" && temp != "오") {
                if (tempIndex == 9) {
                    tempIndex++
                } else {
                    randomList.add(tempIndex++, temp)
                }
                index10 = temp
            }

        }
        randomList.add(9, "왼")
        randomList.add(10, index10)
        randomList.add(11, "오")

        return randomList
    }

    /**
     * 프리퍼런스에 값 저장
     */
    fun setPreference(key: String, str: String) {
        val pref = getSharedPreferences(key, MODE_PRIVATE)
        val editor = pref.edit().apply { putString(key, str) }
        editor.apply()
    }

    /**
     * 프리퍼런스 값 가져오기
     */
    fun getPreference(key: String): String {
        val pref = getSharedPreferences(key, MODE_PRIVATE)
        return pref.getString(key, "")!!
    }


    /**
     * 앱 종료
     */
    fun exit() {
        finishAffinity()
        Handler().postDelayed({
            android.os.Process.killProcess(android.os.Process.myPid())
        }, 200)
    }


    /**
     * 사진 각도
     * exifOrientation: Int
     * return Int
     */
    fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    /**
     * 각도 회전
     * bitmap: Bitmap
     * degree: Float
     * return Bitemap
     */
    fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    /**
     * 캘린더 가져오기
     */

    fun getCalendar(currentDate: LocalDate): ArrayList<CalendarDayInfo> {
        var day: CalendarDayInfo
        val arrayListDayInfo = ArrayList<CalendarDayInfo>()
        var calendar = currentDate.withDayOfMonth(1)    //1일로 변경
        var dayOfWeek = calendar.dayOfWeek.get(ChronoField.DAY_OF_WEEK) //1일의 요일 구하기
        val thisMonthLastDay = calendar.withDayOfMonth(calendar.month.length(calendar.isLeapYear)).get(ChronoField.DAY_OF_MONTH)

        //현재 달의 1일이 무슨 요일인지 검사
        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek += 7
        }

        //현재 달력화면에서 보이는 지난달의 시작일
        calendar = calendar.minusDays((dayOfWeek).toLong())
        for (i in 0 until dayOfWeek) {
            day = CalendarDayInfo()
            day.setDate(calendar)
            day.setInMonth(false)
            arrayListDayInfo.add(day)

            calendar = calendar.plusDays(1)

        }

        for (i in 1..thisMonthLastDay) {
            day = CalendarDayInfo()
            day.setDate(calendar)
            day.setInMonth(true)
            arrayListDayInfo.add(day)

            calendar = calendar.plusDays(1)
        }

        for (i in 1 until 42 - (thisMonthLastDay + dayOfWeek) + 1) {
            day = CalendarDayInfo()
            day.setDate(calendar)
            day.setInMonth(false)
            arrayListDayInfo.add(day)

            calendar = calendar.plusDays(1)
        }

        return arrayListDayInfo

    }


    /**
     * show loading
     */
    open fun showLoading(): Boolean {
        try {
            LogUtil.d("<><><><> showLoadingBar start <><><><>")
            CustomLoadingBar.showLoadingBar(mContext)
        } catch (e: java.lang.Exception) {
            LogUtil.e("<><><><> showLoadingBar Exception : " + e.message)
            return false
        }
        return true
    }

    /**
     * hide loading
     */
    open fun hideLoading(): Boolean {
        try {
            LogUtil.d("<><><><> hideLoadingBar start <><><><>")
            CustomLoadingBar.hideLoadingBar()
        } catch (e: java.lang.Exception) {
            LogUtil.e("<><><><> hideLoadingBar Exception : " + e.message)
            return false
        }
        return true
    }


    open fun replaceContainerFragment(layoutId: Int, fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val classNameTag: String = fragment.javaClass.simpleName
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)

        fragmentTransaction.add(layoutId, fragment, classNameTag).commitAllowingStateLoss()

    }

    open fun replaceContainerFragment(layoutId: Int, fragment: Fragment, i: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val classNameTag: String = fragment.javaClass.simpleName
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        fragmentTransaction.replace(layoutId, fragment, classNameTag).commit()

    }

    /**
     * 키패드숨김
     */
    fun hideKeyboard(edt: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt.windowToken, 0)
    }

    fun showNetworkErrDialog(mContext: Context) {
        val customDialog = CustomDialog(mContext)
        customDialog.setBottomDialog(
                getString(R.string.network_Err),
                getString(R.string.btnConfirm),
                View.OnClickListener {
                    exit()
                    customDialog.dismiss()
                })
        customDialog.show()
    }

    fun showSessionOutDialog(mContext: Context) {
        val customDialog = CustomDialog(mContext)
        customDialog.setBottomDialog(
                getString(R.string.session_Err),
                getString(R.string.btnConfirm),
                View.OnClickListener {
                    exit()
                    customDialog.dismiss()
                })
        customDialog.show()
    }

}
