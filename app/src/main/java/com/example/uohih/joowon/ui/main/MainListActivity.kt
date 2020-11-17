package com.example.uohih.joowon.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.BackPressCloseHandler
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivityMainListBinding
import com.example.uohih.joowon.databinding.ListItemMainListBinding
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.customView.DraggableFloatingButton
import com.example.uohih.joowon.ui.setting.SettingActivity
import com.example.uohih.joowon.ui.worker.WorkerInsertActivity
import com.example.uohih.joowon.ui.worker.WorkerMainActivity
import com.example.uohih.joowon.util.LogUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.list_item_main_list.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.rambler.libs.swipe_layout.SwipeLayout
import java.time.LocalDate


class MainListActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private val mainListViewModel: MainListViewModel by viewModel()
    private lateinit var binding: ActivityMainListBinding

    // back key exit
    private lateinit var backPressCloseHandler: BackPressCloseHandler

    private lateinit var edtSearch: EditText
    private lateinit var btnSearchDelete: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

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


        binding = DataBindingUtil.setContentView<ActivityMainListBinding>(thisActivity, R.layout.activity_main_list)
        binding.run {
            lifecycleOwner = thisActivity
            mainListVm = mainListViewModel
        }

        initView()
    }


    override fun onResume() {
        super.onResume()
        edtSearch.setText("")

        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW3001)
        jsonObject.addProperty("year", LocalDate.now().year.toString())
        mainListViewModel.getEmployeeList(jsonObject)
    }

    private fun initView() {
        edtSearch = binding.mainListEdtSearch
        btnSearchDelete = binding.mainListBtnSearchDelete
        recyclerView = binding.mainListRecyclerView
        tvEmpty = binding.mainListTvEmpty

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mainListViewModel.getSearchResultList(edtSearch.text.toString())
            }

            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                btnSearchDelete.visibility =
                        if (charSequence.isNotEmpty()) View.VISIBLE
                        else View.GONE
            }

        })


        // 직원추가하기
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
        with(mainListViewModel) {
            isNetworkErr.observe(thisActivity, Observer {
                if (it) {
                    showNetworkErrDialog(mContext)
                }
            })

            isLoading.observe(thisActivity, Observer {
                when {
                    it -> showLoading()
                    else -> hideLoading()
                }
            })

            jw3001Data.observe(thisActivity, Observer {
                if ("ZZZZ" == it.errCode) {
                    showSessionOutDialog(thisActivity)
                    return@Observer
                }
                setData()
            })

            searchData.observe(thisActivity, Observer {
                if (it) {
                    setData()
                }
            })
        }
    }


    override fun onBackPressed() {
        backPressCloseHandler.onBackPressed()
    }

    fun onClickMainList(view: View) {
        if (view == btnSearchDelete) {
            edtSearch.setText("")
        }
    }


    /**
     * 직원리스트 set
     */
    private fun setData() {
        tvEmpty.visibility = if (mainListViewModel.searchEmployeeList.size == 0) View.VISIBLE else View.GONE
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

                holder.itemView.mainList_item.setOnClickListener {
                    val intent = Intent(thisActivity, WorkerMainActivity::class.java)
                    intent.putExtra("_id", mainListViewModel.searchEmployeeList[position]._id)
                    startActivity(intent)
                }

                holder.itemView.mainList_swipeLayout.setOnSwipeListener(
                        object : SwipeLayout.OnSwipeListener {
                            override fun onRightStickyEdge(swipeLayout: SwipeLayout?, moveToRight: Boolean) {

                            }

                            override fun onBeginSwipe(swipeLayout: SwipeLayout?, moveToRight: Boolean) {

                            }

                            override fun onLeftStickyEdge(swipeLayout: SwipeLayout?, moveToRight: Boolean) {

                            }

                            override fun onSwipeClampReached(swipeLayout: SwipeLayout?, moveToRight: Boolean) {
                                val jsonObject = JsonObject()
                                jsonObject.addProperty("methodid", Constants.JW3004)
                                jsonObject.addProperty("_id", mainListViewModel.searchEmployeeList[position]._id)

                                with(mainListViewModel){
                                    deleteEmployee(jsonObject, position)

                                    jw3004Data.observe(thisActivity, Observer {
                                        holder.itemView.mainList_swipeLayout.animateReset()
                                        notifyItemRemoved(position)
                                        this.setLiveEmployeeList(this.searchEmployeeList)
                                    })
                                }
                            }
                        }
                )
//
//               val imageBasicPath =  mainListViewModel.jw3001Data.value?.resbody?.employeeList?.get(position)?.profile_image
//                if (!imageBasicPath.isNullOrEmpty()) {
//                    val imagePath = ApiService.Base_URL_ORIGIN + mainListViewModel.jw3001Data.value?.resbody?.employeeList?.get(position)?.profile_image
//                    Glide.with(mContext)
//                            .asBitmap()
//                            .load(imagePath).error(R.drawable.people)
//                            .apply(RequestOptions().circleCrop())
//                            .into(holder.itemView.mainList_imgPeople)
//
//                }
            }
        }
    }
}
