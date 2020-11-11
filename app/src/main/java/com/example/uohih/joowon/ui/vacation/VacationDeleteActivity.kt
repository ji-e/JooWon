package com.example.uohih.joowon.ui.vacation

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivityVacationDeleteBinding
import com.example.uohih.joowon.model.VacationList
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.util.LogUtil
import com.google.gson.JsonObject

class VacationDeleteActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private lateinit var vacationViewModel: VacationViewModel
    private lateinit var binding: ActivityVacationDeleteBinding

    private var _id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation_delete)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_vacation_delete)
        binding.run {
            vacationViewModel = ViewModelProvider(thisActivity, VacationViewModelFactory()).get(VacationViewModel::class.java)
            lifecycleOwner = thisActivity
            vacationDeleteVm = vacationViewModel
        }

        val arg = intent
        if (arg.extras != null) {
            vacationViewModel.setVacationInfo(arg.extras!!)
            _id = arg.getStringExtra("_id")
        }

        initView()
    }

    private fun initView() {
        vacationViewModel.setEmployeeInfo(_id)

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

        vacationViewModel.jw4004Data.observe(thisActivity, Observer {
            val jw4004Data = it ?: return@Observer

            if ("ZZZZ" == jw4004Data.errCode) {
                showSessionOutDialog(thisActivity)
                return@Observer
            }

            val customDialog = CustomDialog(mContext).apply {
                setBottomDialog(
                        getString(R.string.dialog_title),
                        getString(R.string.vacation_delete_dialog_msg),
                        null,
                        getString(R.string.btnConfirm),
                        View.OnClickListener {
                            dismiss()
                            finish()
                        })
            }
            customDialog.show()
        })
    }

    fun onClickVacationDelete(view: View) {
        if (view == binding.vacationDeleteLayDelete) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("methodid", Constants.JW4004)
            vacationViewModel.deleteVacation(jsonObject)
        }

    }
}
