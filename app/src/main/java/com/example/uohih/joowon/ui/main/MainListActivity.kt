package com.example.uohih.joowon.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.BackPressCloseHandler
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.databinding.ActivityMainListBinding
import com.example.uohih.joowon.databinding.ListItemMainListBinding
import com.example.uohih.joowon.model.JW0000
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.repository.ApiService
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.customView.DraggableFloatingButton
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.ui.setting.SettingActivity
import com.example.uohih.joowon.ui.worker.WorkerInsertActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.list_item_main_list.view.*
import org.json.JSONObject


class MainListActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }

    private lateinit var mainListViewModel: MainListViewModel
    private lateinit var binding: ActivityMainListBinding

    // back key exit
    private lateinit var backPressCloseHandler: BackPressCloseHandler

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
        binding.run {
            mainListViewModel = ViewModelProviders.of(this@MainListActivity, MainListViewModelFactory()).get(MainListViewModel::class.java)
            lifecycleOwner = this@MainListActivity
            mainListVm = mainListViewModel
        }

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

        binding.mainListBtnPlus.setDFBtnListener(object : DraggableFloatingButton.DFBtnListener {
            override fun onDFBtnDrag() {

            }

            override fun onDFBBtnClick() {
                val intent = Intent(thisActivity, WorkerInsertActivity::class.java)
                startActivity(intent)
            }

        })


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
//            employeeList = jw3001Data.resbody?.employeeList

            setData()
        })

    }


    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }

    fun onClickMainList(view: View) {
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
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = object : BaseRecyclerView.Adapter<JW3001ResBodyList, ListItemMainListBinding>(
                layoutResId = R.layout.list_item_main_list,
                bindingVariableId = BR.mainListItem
        ) {
            override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<ListItemMainListBinding>, position: Int) {
                super.onBindViewHolder(holder, position)

                holder.itemView.mainList_imgPeople.setOnClickListener {
                    val intent = Intent(thisActivity, PictureActivity::class.java)
                    thisActivity.startActivity(intent)
                }
               val imageBasicPath =  mainListViewModel.jw3001Data.value?.resbody?.employeeList?.get(position)?.profile_image
                if (!imageBasicPath.isNullOrEmpty()) {
                    val imagePath = ApiService.Base_URL_ORIGIN + mainListViewModel.jw3001Data.value?.resbody?.employeeList?.get(position)?.profile_image
                    Glide.with(mContext)
                            .asBitmap()
                            .load(imagePath)
                            .apply(RequestOptions().circleCrop())
                            .into(holder.itemView.mainList_imgPeople)
                }
            }
        }


//        recyclerView.mainList_imgPeople.visibility = View.GONE
//            mAdapter.setClickListener(object : MainListAdapter.ClickListener {
//                override fun onmClickEvent(bundle: Bundle) {
//                    showLoading()
//                    val intent = Intent(mContext, WorkerMainActivity::class.java)
//                    intent.putExtra("worker", bundle)
//                    startActivity(intent)
//                }
//
//            })
//        }

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
