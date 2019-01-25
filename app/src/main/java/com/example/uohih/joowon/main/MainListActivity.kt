package com.example.uohih.joowon.main

import android.content.Intent
import android.os.Bundle
import com.example.uohih.dailylog.base.JWBaseActivity
import com.example.uohih.dailylog.database.DBHelper
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.MainListAdapter
import com.example.uohih.joowon.adapter.StaffData
import kotlinx.android.synthetic.main.activity_main_list.*

class MainListActivity : JWBaseActivity() {

    val dbHelper= DBHelper(this)

   lateinit var mAadapter: MainListAdapter

    // 리스트 뷰
    var mainList = arrayListOf<StaffData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        main_list_btn_plus.setOnClickListener {
            var intent = Intent(this, WorkerInsertActivity::class.java)
            startActivity(intent)
        }



    }


    override fun onResume() {
        super.onResume()
        setData()
    }


    /**
     * db에서 데이터 가져온 후 set
     */
    private fun setData(){
        val cursor =  dbHelper.selectAll(dbHelper.tableNameJW)

        mainList.clear()

        while (cursor.moveToNext()) {
            mainList.add(StaffData(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),cursor.getInt(4),cursor.getInt(5)))
        }

        mAadapter = MainListAdapter(this, mainList)
        main_list_list_view.adapter = mAadapter
    }


}
