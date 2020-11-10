package com.example.uohih.joowon.ui.vacation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uohih.joowon.R
import com.example.uohih.joowon.model.VacationList
import com.example.uohih.joowon.util.LogUtil
import com.google.gson.Gson

class VacationDeleteActivity : AppCompatActivity() {

    lateinit var vacationInfo: VacationList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation_delete)


        vacationInfo = VacationList(
                intent.getStringExtra("vacation_date"),
                intent.getStringExtra("vacation_content"),
                intent.getStringExtra("vacation_cnt"),
                intent.getStringExtra("_id"),
                intent.getStringExtra("vacation_id")
        )

        LogUtil.e(vacationInfo)
    }
}
