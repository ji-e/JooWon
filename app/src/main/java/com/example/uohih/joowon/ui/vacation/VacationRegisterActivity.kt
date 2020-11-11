package com.example.uohih.joowon.ui.vacation

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivityVacationRegisterBinding
import com.example.uohih.joowon.databinding.ListItemVacationBinding
import com.example.uohih.joowon.model.VacationList
import com.example.uohih.joowon.ui.adapter.BaseRecyclerView
import com.example.uohih.joowon.ui.customView.CalendarDialog
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.util.LogUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.btn_positive_bottom.view.*
import kotlinx.android.synthetic.main.btn_white.view.*
import kotlinx.android.synthetic.main.list_item_vacation.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class VacationRegisterActivity : JWBaseActivity(), View.OnClickListener {
    private lateinit var thisActivity: VacationRegisterActivity

    private lateinit var vacationViewModel: VacationViewModel
    private lateinit var binding: ActivityVacationRegisterBinding

    private val vacationList = mutableListOf<VacationList>()

    private lateinit var bitmap: String

    private var _id = ""
    private var date = LocalDate.now().toString()
    private var isCalendarDialogShow = false

    private lateinit var btnCalendar: ImageButton
    private lateinit var tvDate: TextView
    private lateinit var tvHalf: TextView
    private lateinit var ckbHalf: CheckBox
    private lateinit var edtContent: EditText
    private lateinit var btnRegisterV: Button
    private lateinit var btnRegister: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivity = this

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_vacation_register)
        binding.run {
            vacationViewModel = ViewModelProvider(thisActivity, VacationViewModelFactory()).get(VacationViewModel::class.java)
            lifecycleOwner = thisActivity
            vacationRegisterVm = vacationViewModel
        }

        val args = intent
        if (args != null) {
            _id = args.getStringExtra("_id")
            vacationViewModel.setEmployeeInfo(_id)

            if (args.hasExtra("date")) {
                date = args.getStringExtra("date")
            }
        }

        initView()

    }

    fun initView() {
        btnCalendar = binding.vacationBtnCalendar
        tvDate = binding.vacationTvDate
        tvHalf = binding.vacationTvHalf
        ckbHalf = binding.vacationCkbHalf
        edtContent = binding.vacationEdtContent
        btnRegisterV = binding.vacationBtnRegisterV.btnWhite
        btnRegister = binding.vacationBtnRegister.btnPositive
        recyclerView = binding.vacationRecyclerViewE

        btnRegisterV.text = getString(R.string.vacation_add)
        btnRegister.text = getString(R.string.vacation_title)
        tvDate.text = "$date ~ $date"

        btnCalendar.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        tvHalf.setOnClickListener(this)
        btnRegisterV.setOnClickListener(this)
        btnRegister.setOnClickListener(this)

        setRecyclerView()

        setObserve()
    }


    private fun setObserve() {
        // 네트워크에러
        vacationViewModel.isNetworkErr.observe(thisActivity, Observer {
            val isNetworkErr = it ?: return@Observer
            if (isNetworkErr) {
                showNetworkErrDialog(mContext)
            }
        })

        // 로딩
        vacationViewModel.isLoading.observe(thisActivity, Observer {
            val isLoading = it ?: return@Observer

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        vacationViewModel.jw4001Data.observe(thisActivity, Observer {
            val jw4001Data = it ?: return@Observer
            val customDialog = CustomDialog(mContext)
            if ("ZZZZ" == jw4001Data.errCode) {
                showSessionOutDialog(thisActivity)
                return@Observer
            }

            if (jw4001Data.resbody == null) {
                customDialog.apply {
                    setBottomDialog(
                            getString(R.string.dialog_title),
                            getString(R.string.vacation_dialog_err),
                            null,
                            getString(R.string.btnConfirm),
                            View.OnClickListener {
                                dismiss()
                            })
                }
                customDialog.show()
                return@Observer
            }

            customDialog.apply {
                setBottomDialog(
                        getString(R.string.dialog_title),
                        getString(R.string.vacation_dialog_msg),
                        null,
                        getString(R.string.btnConfirm),
                        View.OnClickListener {
                            finish()
                            dismiss()
                        })
            }
            customDialog.show()
        })

    }

    private fun setRecyclerView() {
        vacationViewModel.setInitVacationList()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = object : BaseRecyclerView.Adapter<VacationList, ListItemVacationBinding>(
                layoutResId = R.layout.list_item_vacation,
                bindingVariableId = BR.vacationRItem
        ) {
            override fun onBindViewHolder(holder: BaseRecyclerView.ViewHolder<ListItemVacationBinding>, position: Int) {
                super.onBindViewHolder(holder, position)

                holder.itemView.item_vacationR_delete.setOnClickListener {
                    vacationList.removeAt(position)
                    vacationViewModel.addVacationList(vacationList)
                    setRegisterBtnName()
                }

            }
        }
    }


    override fun onClick(v: View) {
        when (v) {
            btnCalendar, tvDate -> {
                if (!isCalendarDialogShow) {
                    showCalendarDialog(LocalDate.now().toString())
                }
            }
            tvHalf -> {
                ckbHalf.isChecked = !ckbHalf.isChecked
            }
            btnRegisterV -> {
                // 휴가 리스트에 추가
                hideKeyboard(edtContent)
                addVacationList()
            }
            btnRegister -> {
                // 휴가등록
                registerVacation()
            }
        }
    }

    private fun registerVacation() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW4001)
        vacationViewModel.registerVacation(jsonObject)
    }

    /**
     * 휴가리스트 추가
     */
    private fun addVacationList() {
        val dateArr = tvDate.text.toString().split("~")
        val weekdayList = calDateWeekday(dateArr[0], dateArr[1])

        val isHalf = if (ckbHalf.isChecked) "0.5" else "1.0"
        val contentTxt =
                if (edtContent.text.toString().isNullOrEmpty()) {
                    getString(R.string.vacation_content2)
                } else {
                    edtContent.text.toString()
                }

        for (i in 0 until weekdayList.size) {
            for (j in 0 until vacationList.size) {
                if (vacationList[j].vacation_date == weekdayList[i]) {
                    vacationList.removeAt(j)
                }
            }
            vacationList.add(VacationList(weekdayList[i], contentTxt, isHalf, null, null))
        }

        vacationViewModel.addVacationList(vacationList)

        setRegisterBtnName()

    }


    private fun setRegisterBtnName() {
        var totalVacationCnt = 0f
        for (i in 0 until vacationList.size) {
            if (vacationList[i].vacation_cnt != null) {
                totalVacationCnt += vacationList[i].vacation_cnt!!.toFloat()
            }
        }

        val btnTxt =
                if (totalVacationCnt > 0) {
                    String.format(getString(R.string.vacation_btn), totalVacationCnt.toString())
                } else {
                    getString(R.string.vacation_title)
                }

        btnRegister.text = btnTxt

    }

    /**
     * 날짜 사이 평일 수 구하기
     */
    private fun calDateWeekday(date1: String, date2: String): ArrayList<String> {
        val weekdayList = arrayListOf<String>()
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            var workingDays = 0.0

            start.time = sdf.parse(date1)
            end.time = sdf.parse(date2)

            while (!start.after(end)) {
                val day = start.get(Calendar.DAY_OF_WEEK)
                if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
                    workingDays++
                    weekdayList.add(sdf.format(start.time))
                }
                start.add(Calendar.DATE, 1)

            }
            return weekdayList

        } catch (e: ParseException) {
            LogUtil.e(e)
            return weekdayList
        }
    }


    /**
     * 캘린더 다이얼로그
     */
    private fun showCalendarDialog(date: String) {
        val calendarDialog = CalendarDialog(mContext).apply {
            setBottomDialog(
                    date,
                    null,
                    object : CalendarDialog.ConfirmBtnClickListener {
                        override fun onConfirmClick(date: ArrayList<LocalDate>) {
                            if (date.size > 0) {
                                val strDate = date[0].toString() + " ~ " + date[date.size - 1].toString()
                                tvDate.text = strDate
                            }
                        }
                    },
                    isFutureSelect = true,
                    isSelectedMulti = false,
                    isSelectedRang = true)
        }

        calendarDialog.show()
        isCalendarDialogShow = true

        calendarDialog.setOnDismissListener {
            isCalendarDialogShow = false
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard(edtContent)
    }
}