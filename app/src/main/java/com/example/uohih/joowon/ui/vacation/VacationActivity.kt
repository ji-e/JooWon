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


class VacationActivity : JWBaseActivity(), View.OnClickListener {
    private val thisActivity by lazy { this }

    private lateinit var vacationViewModel: VacationViewModel
    private lateinit var binding: ActivityVacationBinding

    private val base = JWBaseApplication()
    private val dbHelper = DBHelper(this)

    // 리스트 뷰
    private var mainList = arrayListOf<StaffData>()
    private var subList = arrayListOf<StaffData>()
    private val vacationList = arrayListOf<String>()
    private var checkBoxList = arrayListOf<Boolean>(false)

    private val todayJson = DateCommonUtil().getToday().get("yyyymmdd").toString()

    private lateinit var mVacationSearchAdapter: VacationSearchAdapter

    private lateinit var edtSearch: EditText
    private lateinit var btnSearchDelete: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var layVacationFram: FrameLayout
    private lateinit var tvEmpty: TextView

    private var cntSchedule = 0.0           //사용예정휴가일수
    private var cntRemain = 0.0             //남은휴가일수
    private var cntTotal = ""               //총휴가일수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_vacation)
        binding.run {
            vacationViewModel = ViewModelProviders.of(thisActivity, VacationViewModelFactory()).get(VacationViewModel::class.java)
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
//        edtSearch.setOnTouchListener { view, motionEvent ->
//
//            if (motionEvent.action == MotionEvent.ACTION_UP) {
//                recyclerView.visibility = View.VISIBLE
//                layVacationFram.visibility = View.GONE
//            }
//            false
//        }

        setObserve()
    }

    private fun setObserve() {
        vacationViewModel.liveEmployeeList.observe(thisActivity, Observer {
            val liveEmployeeList = it ?: return@Observer
            layVacationFram.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            setData()
        })
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

//    inner class VacationListItemClickListener : OnItemClickListener {
//        override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//
//            cntRemain = (subList[position].total.toDouble() - subList[position].use.toDouble())
//            cntTotal = subList[position].total
//
//            val imm= getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(vacation_edt_name.windowToken, 0)
//
//            val bundle = Bundle()
//            bundle.putString("name", subList[position].name)
//            bundle.putString("join", subList[position].joinDate.toString())
//            bundle.putString("phone", subList[position].phone)
//            bundle.putString("vacation", subList[position].use + "/" + subList[position].total)
//            bundle.putString("bitmap", subList[position].picture)
//
//            bundle.putDouble("cntRemain", cntRemain)
//            bundle.putString("cntTotal", cntTotal)
//
//            replaceContainerFragment(R.id.vacation_frame_ly, VacationFragment.newInstance(bundle), position)
//
//            vacation_ly_search.visibility = View.GONE
//            vacation_frame_ly.visibility = View.VISIBLE
//        }
//
//    }

    override fun onClick(view: View) {
        when (view) {
//            edtSearch -> {
//                recyclerView.visibility = View.VISIBLE
//                layVacationFram.visibility = View.GONE
//            }
//            R.id.vacation_btn_delete1 ->{
//                vacation_edt_name.setText("")
//            }
//            R.id.vacation_btn_search -> {
//                setSearch(vacation_edt_name.text.toString())
//            }

        }
    }

    /**
     * db에서 데이터 가져온 후 set
     */
//    private fun setData() {
//        mainList.clear()
//        val dbHelper = DBHelper(this)
//        val cursor = dbHelper.selectAll(dbHelper.tableNameWorkerJW)
//
//        while (cursor.moveToNext()) {
//            mainList.add(StaffData(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
//        }
//    }


    /**
     * 검색한 후 set
     * 문자 입력시마다 리스트를 지우고 새로 뿌려줌
     */
//    private fun setSearch(charText: String) {
//        vacation_frame_ly.visibility = View.GONE
//        vacation_ly_search.visibility = View.VISIBLE
//
//        subList.clear()
//        for (i in 0 until mainList.size) {
//            // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환
//            if (mainList[i].name.toLowerCase().contains(charText) || mainList[i].phone.toLowerCase().contains(charText)) {
//                // 검색된 데이터를 리스트에 추가한다.
//                subList.add(mainList[i])
//            }
//        }
//
//        if (subList.size > 0) {
//            vacation_tv_nothing.visibility = View.GONE
//            vacation_listview_search.visibility = View.VISIBLE
//        }
//
//        mVacationSearchAdapter = VacationSearchAdapter(this, subList)
//        vacation_listview_search.adapter = mVacationSearchAdapter
//    }
//
//
//    fun clearEditName() {
//        vacation_edt_name.setText("")
//    }

    override fun onStop() {
        super.onStop()
        hideKeyboard(edtSearch)
    }
}

