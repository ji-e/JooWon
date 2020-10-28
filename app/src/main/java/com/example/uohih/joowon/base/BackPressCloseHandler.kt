package com.example.uohih.joowon.base

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.uohih.joowon.R

/**
 * back key 두번 클릭 시 앱 종료
 * mContext: Context
 */
class BackPressCloseHandler(mContext: Context) {
    private var mContext = mContext
    private var backKeyPressedTime: Long = 0
    lateinit var toast: Toast

    fun onBackPressed() {
        // 한번 눌렀을 때
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(mContext, mContext.getString(R.string.exit_msg), Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // 2초 안에 두번 눌렀을 때
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            (mContext as Activity).finishAffinity()
            android.os.Process.killProcess(android.os.Process.myPid())
            toast.cancel()
        }

    }

}