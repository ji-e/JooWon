package com.example.uohih.joowon.util

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.model.VacationList
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UICommonUtil {
    companion object {
        private val uiCommonHandler = Handler()

        var decimalFormat = DecimalFormat("#,###")

        fun setCaptureActivity(mActivity: Activity) {}

        fun setTalkbackFocus(mView: View) {
            uiCommonHandler.postDelayed({ mView.sendAccessibilityEvent(8) }, 1000L)
        }


        fun isEmpty(str: String?): Boolean {
            return str == null || str.trim { it <= ' ' }.isEmpty()
        }

        fun getDateFormat(timeStr: String): String {
            val formatTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            if (isEmpty(timeStr)) {
                val time = Date()
                return formatTime.format(time)
            } else if (timeStr.length == 14) {
                val retValue = StringBuffer("")
                retValue.append(timeStr.substring(0, 4))
                retValue.append("-")
                retValue.append(timeStr.substring(4, 6))
                retValue.append("-")
                retValue.append(timeStr.substring(6, 8))
                retValue.append("  ")
                retValue.append(timeStr.substring(8, 10))
                retValue.append(":")
                retValue.append(timeStr.substring(10, 12))
                retValue.append(":")
                retValue.append(timeStr.substring(12, 14))
                return retValue.toString()
            } else {
                return ""
            }
        }

        fun getPreferencesData(key: String): String {
            val preferences = JWBaseApplication.useSharedPreference.apply {
                setKey(key)
            }
            return preferences.data
        }

        fun setPreferencesData(key: String, value: String) {
            val preferences = JWBaseApplication.useSharedPreference.apply {
                setKey(key)
            }
            preferences.data = value
        }

        fun removePreferencesData(key: String) {
            val preferences = JWBaseApplication.useSharedPreference.apply {
                setKey(key)
            }
            preferences.data = ""
        }

        private var initEmployeeList = mutableListOf<JW3001ResBodyList>()
        fun setInitEmployeeList(initEmployeeList: MutableList<JW3001ResBodyList>) {
            this.initEmployeeList = initEmployeeList
        }

        fun getInitEmployeeList(): MutableList<JW3001ResBodyList> {
            return initEmployeeList
        }

        fun setEmployeeInfo(_id: String, bundle: Bundle, useVacation: ArrayList<VacationList>?) {
            for (i in 0 until initEmployeeList.size) {
                val info = initEmployeeList[i]
                if (info._id.toString() == _id) {
                    initEmployeeList[i] = (JW3001ResBodyList(
                            _id = info._id,
                            profile_image = bundle.getString("profile_image", info.profile_image),
                            name = bundle.getString("name", info.name),
                            phone_number = bundle.getString("phone_number", info.phone_number),
                            birth = bundle.getString("birth", info.birth),
                            entered_date = bundle.getString("entered_date", info.entered_date),
                            total_vacation_cnt = bundle.getString("total_vacation_cnt", info.total_vacation_cnt),
                            use_vacation = useVacation ?: info.use_vacation,
                            use_vacation_cnt = bundle.getString("use_vacation_cnt", info.use_vacation_cnt)
                    ))
                    return
                }
            }
        }

        fun getEmployeeInfo(_id: String): JW3001ResBodyList? {
            var employeeInfo: JW3001ResBodyList? = null
            for (i in 0 until initEmployeeList.size) {
                val info = initEmployeeList[i]
                if (info._id == _id) {
                    employeeInfo = (JW3001ResBodyList(
                            _id = info._id,
                            profile_image = info.profile_image,
                            name = info.name,
                            phone_number = info.phone_number,
                            birth = info.birth,
                            entered_date = info.entered_date,
                            total_vacation_cnt = info.total_vacation_cnt,
                            use_vacation = info.use_vacation,
                            use_vacation_cnt = info.use_vacation_cnt

                    ))
                    return employeeInfo
                }
            }
            return employeeInfo
        }

        fun getVacationInfo(calendarDate: String, vacationList: ArrayList<VacationList>): VacationList? {
            for (i in vacationList.indices) {
                if (calendarDate == vacationList[i].vacation_date.toString()) {
                    return VacationList(vacationList[i].vacation_date, vacationList[i].vacation_content, vacationList[i].vacation_cnt)
                }
            }
            return null
        }

    }


}
