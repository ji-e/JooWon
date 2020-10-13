package com.example.uohih.joowon.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.ui.adapter.MainListAdapter
import com.example.uohih.joowon.ui.adapter.StaffData
import com.example.uohih.joowon.base.BackPressCloseHandler
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.databinding.ActivityMainListBinding
import com.example.uohih.joowon.model.JW0000
import com.example.uohih.joowon.model.JW3001
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.ui.customView.DraggableFloatingButton
import com.example.uohih.joowon.ui.setting.SettingActivity
import com.example.uohih.joowon.ui.worker.WorkerInsertActivity
import com.example.uohih.joowon.ui.worker.WorkerMainActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main_list.*
import org.json.JSONObject

//import sun.jvm.hotspot.utilities.IntArray
//import javax.swing.UIManager.put


class MainListActivity : JWBaseActivity() {
    private val base = JWBaseApplication()
    private val dbHelper = DBHelper(this)

    private val thisActivity by lazy { this }

    private lateinit var mainListViewModel: MainListViewModel
    private lateinit var binding: ActivityMainListBinding
    private lateinit var mAdapter: MainListAdapter

    // back key exit
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    // 리스트 뷰
    private var mainList = arrayListOf<StaffData>()

    private var employeeList: MutableList<JW3001.resbodyLst>? = null

    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)

        // back key exit 초기화
        backPressCloseHandler = BackPressCloseHandler(this)

        val boardId =
                if (intent.action == Intent.ACTION_VIEW) intent.data?.getQueryParameter("key").toString()
                else ""
        LogUtil.e(boardId)

        if ("setting" == boardId) {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }


        binding = DataBindingUtil.setContentView<ActivityMainListBinding>(this, R.layout.activity_main_list)

        mainListViewModel = ViewModelProviders.of(this, MainListViewModelFactory()).get(MainListViewModel::class.java)

        binding.mainListVm = mainListViewModel
        binding.lifecycleOwner = this


        binding.mainListBtnPlus.setDFBtnListener(object : DraggableFloatingButton.DFBtnListener {
            override fun onDFBtnDrag() {

            }

            override fun onDFBBtnClick() {
                val intent = Intent(thisActivity, WorkerInsertActivity::class.java)
                startActivity(intent)
            }

        })
        initView()
    }


    override fun onResume() {
        super.onResume()

        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW3001)
        mainListViewModel.getEmployeeList(jsonObject)
    }

    private fun initView() {
        recyclerView = binding.mainListRecyclerView
        setObserve()
    }

    private fun setObserve() {
        mainListViewModel.isLoading.observe(this@MainListActivity, Observer {
            val isLoading = it ?: return@Observer

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        mainListViewModel.jw3001Data.observe(this@MainListActivity, Observer {
            val jw3001Data = it ?: return@Observer
            employeeList = jw3001Data.resbody?.employeeList

            setData()
        })

    }


    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }

    fun onClickMainList(view: View) {
//        if (view==binding.mainListBtnPlus) {
//            val intent = Intent(this, WorkerInsertActivity::class.java)
//            startActivity(intent)
//        } else
        if (view == binding.mainListBtnSignout) {
            signOut(this)
        } else if (view == binding.mainListBtnSession) {
            ssss()
        }
    }


    /**
     * db에서 데이터 가져온 후 set
     */
    private fun setData() {
//        val cursor = dbHelper.selectAll(dbHelper.tableNameWorkerJW)
//
//        mainList.clear()
//
//        while (cursor.moveToNext()) {
//            mainList.add(StaffData(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
//        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (employeeList != null) {
            mAdapter = MainListAdapter(employeeList!!)
            recyclerView.adapter = mAdapter
            mAdapter.setClickListener(object : MainListAdapter.ClickListener {
                override fun onmClickEvent(bundle: Bundle) {
                    showLoading()
                    val intent = Intent(mContext, WorkerMainActivity::class.java)
                    intent.putExtra("worker", bundle)
                    startActivity(intent)
                }

            })
        }

//        main_list_recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }


    fun ssss() {
        JWBaseRepository().requestSignInService(JsonObject(), object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw0000Data = Gson().fromJson(data.toString(), JW0000::class.java)
                if ("N" == jw0000Data.result) {
                    return
                }

            }

            override fun onFailure(code: Int) {

            }

            override fun onError(throwable: Throwable) {

            }

        })
    }


}
