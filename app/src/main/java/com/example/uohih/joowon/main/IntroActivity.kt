package com.example.uohih.joowon.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.setting.PasswordCheckActivity
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.LogUtil
import kotlinx.android.synthetic.main.activity_intro.*


/**
 * 인트로
 * 2초 후 MainListActivity()로 이동
 */
class IntroActivity : JWBaseActivity() {
    lateinit var mListener: mPermissionListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        //step1
        permissionCheck()


    }

    /**
     * step1
     * permissionCheck
     * 퍼미션 체크 (저장공간, 카메라)
     */
    private fun permissionCheck() {
        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE)
        val arrayList = ArrayList<String>()

        for (i in 0 until permission.size) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission[i])

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                arrayList.add(permission[i])
            }
        }

        val arry = arrayOfNulls<String>(arrayList.size)
        for (i in 0 until arrayList.size) {
            arry[i] = arrayList[i]
        }

        if (!arrayList.isEmpty()) {
            ActivityCompat.requestPermissions(this, arry, 0)

            setmPermissionListener(object : mPermissionListener {
                override fun onFail() {
                    finish()
                }

                override fun onSuccess() {
                    // step2
                    nextActivity()
                }
            })

        }

        if (arrayList.isEmpty()) {
            // step2
            nextActivity()
        }
    }


    /**
     * step2
     * 다음 액티비티로 이동
     */
    fun nextActivity() {
        intro_activity.postDelayed({
            LogUtil.d((getPreference(Constants.passwordSetting)))

            //설정된 비밀번호가 존재할 경우
            if ("" != getPreference(Constants.passwordSetting)) {
                var intent = Intent(this, PasswordCheckActivity::class.java)
                startActivityForResult(intent, Constants.passwordCheck)
            }
            //설정된 비밀번호가 존재하지 않을 경우
            else goMainActivity()

        }, 2000)
    }


    private fun goMainActivity() {
        val dbHelper = DBHelper(this)
//        LogUtil.d(dbHelper.getTableExiste(dbHelper.tableNameVacationJW).count)
//        if (dbHelper.getTableExiste(dbHelper.tableNameVacationJW).count <= 0)
            dbHelper.createVacationTable()

        val intent = Intent(this, MainListActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.passwordCheck -> {
                    goMainActivity()
                }
            }
        } else {
            exit()
        }
    }

    /** ----------- permission check start ----------*/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    mListener.onFail()
                } else {
                    mListener.onSuccess()
                }
            }
        }
    }

    interface mPermissionListener {
        fun onFail()
        fun onSuccess()
    }

    fun setmPermissionListener(listener: mPermissionListener) {
        mListener = listener
    }
    /** ----------- permission check end ----------*/
}
