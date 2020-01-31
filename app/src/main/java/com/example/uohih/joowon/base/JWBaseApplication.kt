package com.example.uohih.joowon.base

import android.app.Application


class JWBaseApplication : Application() {


    /**
     * ------------- 캘린더 팝업 날짜 세팅 -------------
     */
    fun setSeleteDate(selecteDate: String) {
        Companion.selecteDate = selecteDate
    }

    fun getSeleteDate(): String {
        return selecteDate
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



    companion object {
        private var selecteDate = JWBaseActivity().getToday().get("yyyymmdd").toString()
        private var deleteItem = ArrayList<String>()//삭제 항목
    }

}