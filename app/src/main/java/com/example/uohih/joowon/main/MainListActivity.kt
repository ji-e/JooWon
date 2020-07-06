package com.example.uohih.joowon.main

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.MainListAdapter
import com.example.uohih.joowon.adapter.StaffData
import com.example.uohih.joowon.base.BackPressCloseHandler
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.setting.SettingActivity
import com.example.uohih.joowon.worker.WorkerInsertActivity
import kotlinx.android.synthetic.main.activity_main_list.*

//import sun.jvm.hotspot.utilities.IntArray
//import javax.swing.UIManager.put


class MainListActivity : JWBaseActivity() {
    private val base = JWBaseApplication()
    private val dbHelper = DBHelper(this)

    private lateinit var mAadapter: MainListAdapter

    // back key exit
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    // 리스트 뷰
    var mainList = arrayListOf<StaffData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        // back key exit 초기화
        backPressCloseHandler = BackPressCloseHandler(this)


        main_list_btn_plus.setOnClickListener {
            val intent = Intent(this, WorkerInsertActivity::class.java)
            startActivity(intent)
        }

        val boardId = if (intent.action == Intent.ACTION_VIEW)
            intent.data.getQueryParameter("key").toString()
        else ""
        LogUtil.e(boardId)

        if ("setting" == boardId) {
            val intent = Intent(this, SettingActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
//            finish()

        }
    }


    override fun onResume() {
        super.onResume()
        setData()


    }

    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }


    /**
     * db에서 데이터 가져온 후 set
     */
    private fun setData() {
        val cursor = dbHelper.selectAll(dbHelper.tableNameWorkerJW)

        mainList.clear()

        while (cursor.moveToNext()) {
            mainList.add(StaffData(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
        }

        main_list_recyclerView.setHasFixedSize(true)
        main_list_recyclerView.layoutManager = LinearLayoutManager(this)

        mAadapter = MainListAdapter(mainList)
        main_list_recyclerView.adapter = mAadapter

//        main_list_recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }


}
