package com.example.uohih.joowon.util

import android.app.Activity
import android.os.Handler
import android.view.View
import com.example.uohih.joowon.base.JWBaseApplication
import java.io.Serializable
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class UICommonUtil : Serializable {
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

        fun removePreferencesData(key:String){
            val preferences = JWBaseApplication.useSharedPreference.apply {
                setKey(key)
            }
            preferences.data = ""
        }    }
}
