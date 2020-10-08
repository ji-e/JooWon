package com.example.uohih.joowon.repository

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil

class RetrofitSharedPreferences {

//    private val pref: SharedPreferences
//    fun putHashSet(key: String?, set: HashSet<String>) {
//        val editor = pref.edit()
//        editor.putStringSet(key, set)
//        editor.commit()

//    }

    //    fun getHashSet(key: String?, dftValue: HashSet<String>): HashSet<String> {
//        return try {
//            pref.getStringSet(key, dftValue) as HashSet<String>
//        } catch (e: Exception) {
//            e.printStackTrace()
//            dftValue
//        }
//    }
    fun getddd(application: JWBaseApplication) {

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            //        pref = application.getSharedPreferences("D", Activity.MODE_PRIVATE)
//        JWBaseActivity()
            if (application != null)
                LogUtil.e((application).getHashSet("D", HashSet()))

        }, 0)
    }

    fun getdd() {

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            //            LogUtil.e((JWBaseActivity().getPreference("F")))
//            LogUtil.e(application)
        }, 0)


//        var mContext = JWBaseApplication().applicationContext
//        LogUtil.e(mContext)


    }
//
//    companion object {
//        const val KEY_COOKIE = "cookie"
//         private var dsp: RetrofitSharedPreferences? = null
//
//        fun getInstanceOf(): RetrofitSharedPreferences {
//            if (dsp == null) {
//                dsp = RetrofitSharedPreferences()
//            }
//            return dsp!!
//        }
//    }
//
//    init {
//        val PREF_NAME = "joowonCok"
//        pref = applicationContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
//    }
}