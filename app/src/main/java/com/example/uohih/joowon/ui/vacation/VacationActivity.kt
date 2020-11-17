package com.example.uohih.joowon.ui.vacation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.databinding.ActivityVacationBinding
import com.example.uohih.joowon.databinding.ListItemVacationSearchBinding
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.adapter.StaffData
import com.example.uohih.joowon.ui.adapter.VacationSearchAdapter
import com.example.uohih.joowon.util.DateCommonUtil
import org.koin.androidx.viewmodel.ext.android.viewModel


class VacationActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private val vacationViewModel: VacationViewModel by viewModel()
    private lateinit var binding: ActivityVacationBinding

    private lateinit var edtSearch: EditText
    private lateinit var btnSearchDelete: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var layVacationFram: FrameLayout
    private lateinit var tvEmpty: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_vacation)
        binding.run {
            lifecycleOwner = thisActivity
            vacationVm = vacationViewModel
        }


        initView()

    }

    override fun onResume() {
        super.onResume()

        vacationViewModel.setEmployeeList()

    }

    private fun initView() {

        edtSearch = binding.vacationEdtSearch
        btnSearchDelete = binding.vacationBtnSearchDelete
        recyclerView = binding.vacationRecyclerView
        layVacationFram = binding.vacationLayFrame
        tvEmpty = binding.vacationTvEmpty

        edtSearch.addTextChangedListener(VacationTextWatcher())

        setObserve()
    }

    private fun setObserve() {

        with(vacationViewModel) {
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

            liveEmployeeList.observe(thisActivity, Observer {
                layVacationFram.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                setData()
            })
        }
    }


    fun onClickVacation(view: View) {
        if (view == btnSearchDelete) {
            edtSearch.setText("")
        }
    }

    /**
     * 직원리스트 set
     */
    private fun setData() {
        tvEmpty.visibility = if (vacationViewModel.searchEmployeeList.size == 0) View.VISIBLE else View.GONE
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = object : BaseRecyclerView.Adapter<JW3001ResBodyList, ListItemVacationSearchBinding>(
                layoutResId = R.layout.list_item_vacation_search,
                bindingVariableId = BR.vacationItem
        ) {
            override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<ListItemVacationSearchBinding>, position: Int) {
                super.onBindViewHolder(holder, position)

                holder.itemView.setOnClickListener {
                    hideKeyboard(edtSearch)

                    val intent = Intent(thisActivity, VacationRegisterActivity::class.java)
                    intent.putExtra("_id", vacationViewModel.searchEmployeeList[position]._id)
                    startActivity(intent)
                }

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

    private inner class VacationTextWatcher : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            btnSearchDelete.visibility =
                    if (charSequence.isNotEmpty()) View.VISIBLE
                    else View.GONE
        }

        override fun afterTextChanged(s: Editable) {
            vacationViewModel.getSearchResultList(edtSearch.text.toString())
        }


    }

    override fun onStop() {
        super.onStop()
        hideKeyboard(edtSearch)
    }
}

