package com.example.uohih.joowon.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.database.ExportExcel
import com.example.uohih.joowon.R
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 환경 설정
 */
class SettingActivity : JWBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

    }




    fun btnOnClick(v: View) {
        when (v) {
            setting_layout_password -> {
//                val intent = Intent(this, PasswordSettingActivity::class.java)
//                startActivity(intent)
            }
            setting_layout_excel2 -> {
                val exportExcel=ExportExcel(this)
                exportExcel.readExcelFile()
            }
            setting_layout_excel -> {
                val exportExcel= ExportExcel(this)
                exportExcel.makeExcelFile()
            }
            setting_layout_reset -> {
//                val intent = Intent(this, SettingResetActivity::class.java)
//                startActivity(intent)
            }
        }
    }

}
