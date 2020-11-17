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
import org.koin.androidx.viewmodel.ext.android.viewModel

class VacationDeleteActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private val vacationViewModel: VacationViewModel by viewModel()
    private lateinit var binding: ActivityVacationDeleteBinding

    private var _id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation_delete)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_vacation_delete)
        binding.run {
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

            jw4004Data.observe(thisActivity, Observer {
                if ("ZZZZ" == it.errCode) {
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
    }

    fun onClickVacationDelete(view: View) {
        if (view == binding.vacationDeleteLayDelete) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("methodid", Constants.JW4004)
            vacationViewModel.deleteVacation(jsonObject)
        }

    }
}
