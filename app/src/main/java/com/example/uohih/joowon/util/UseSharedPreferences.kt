package com.example.uohih.joowon.util

import android.app.Activity
import android.content.Context

class UseSharedPreferences(mContext: Context) {

    private var key = "key"
    private var preferences = mContext.getSharedPreferences(mContext.packageName, Activity.MODE_PRIVATE)

    var data: String
        get() = preferences.getString(key, "") ?: ""
        set(value) = preferences.edit().putString(key, value).apply()

    fun setKey(key: String) {
        this.key = key
    }
}