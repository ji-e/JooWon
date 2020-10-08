package com.example.uohih.joowon.base

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import java.util.HashSet


class JWBaseApplication : Application() {


    /**
     * ------------- 캘린더 팝업 날짜 세팅 -------------
     */
    fun setSelectDate(selectDate: String) {
        Companion.selectDate = selectDate
    }

    fun getSelectDate(): String {
        return selectDate
    }

    /**
     * ------------- 삭제 항목 세팅 -------------
     */
    fun setDeleteItem(deleteItem: ArrayList<String>?) {
        Companion.deleteItem.clear()
        if (deleteItem != null) {
            LogUtil.d(deleteItem.toString())
            Companion.deleteItem = deleteItem
        }
    }

    fun getDeleteItem(): ArrayList<String> {
        return deleteItem
    }

    fun putHashSet(key: String?, set: HashSet<String>) {
        val PREF_NAME = "joowonCok"
        val pref = applicationContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)

        val editor = pref.edit()
        editor.putStringSet(key, set)
    }

    fun getHashSet(key: String?, dftValue: HashSet<String>): HashSet<String> {

        return try {
            val PREF_NAME = "joowonCok"
            val pref = getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)

            pref.getStringSet(key, dftValue) as HashSet<String>
        } catch (e: Exception) {
            e.printStackTrace()
            dftValue
        }
    }


    companion object {
        private var selectDate = JWBaseActivity().getToday().get("yyyymmdd").toString()
        private var deleteItem = ArrayList<String>()//삭제 항목

        const val KEY_COOKIE = "cookie"

    }

}