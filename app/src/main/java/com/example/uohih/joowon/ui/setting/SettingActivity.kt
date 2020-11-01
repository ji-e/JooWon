package com.example.uohih.joowon.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.database.ExportExcel
import com.example.uohih.joowon.R
import com.example.uohih.joowon.databinding.ActivitySettingBinding
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.signin.SignInActivity
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 환경 설정
 */
class SettingActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_setting)
        binding.run {
            settingViewModel = ViewModelProviders.of(thisActivity, SettingViewModelFactory()).get(SettingViewModel::class.java)
            lifecycleOwner = thisActivity
            settingVm = settingViewModel
        }

        binding.settingTvVersion.text = getString(R.string.version) + getVersionInfo()
        setObserve()
    }

    private fun setObserve() {
        // 네트워크에러
        settingViewModel.isNetworkErr.observe(thisActivity, Observer {
            val isNetworkErr = it ?: return@Observer
            if (isNetworkErr) {
                showNetworkErrDialog(mContext)
            }
        })

        // 로딩
        settingViewModel.isLoading.observe(thisActivity, Observer {
            val isLoading = it ?: return@Observer

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        settingViewModel.jw2002Data.observe(thisActivity, Observer {
            val jw2002Data = it ?: return@Observer
            if ("false" == jw2002Data.result) {
                //todo
            }
            if ("N" == jw2002Data.resbody?.signOutValid) {
                //todo
            }

            // 자동로그인 토큰 제거
            UICommonUtil.removePreferencesData(Constants.PREFERENCE_AUTO_SIGNIN_TOKEN)

            (mContext as Activity).finish()
            val intent = Intent(mContext, SignInActivity::class.java)
            startActivity(intent)
        })
    }

    fun onClickSetting(v: View) {
        when (v) {
//            setting_layout_password -> {
//                val intent = Intent(this, PasswordSettingActivity::class.java)
//                startActivity(intent)
//            }
//            setting_layout_password -> {
//                val intent = Intent(this, PasswordCheckActivity::class.java)
//                intent.putExtra("PW_RESET", "Y")
//                startActivity(intent)
//            }
            binding.settingLayExcel -> {
                // 엑셀로 내보내기
                val exportExcel = ExportExcel(this)
                exportExcel.makeExcelFile()
            }
            binding.settingLayExcel2 -> {
                // 엑셀 가져오기
                val exportExcel = ExportExcel(this)
                exportExcel.readExcelFile()
            }
            binding.settingTvSignout -> {
                // 로그아웃
                signOut()
            }

//            setting_layout_reset -> {
//                val intent = Intent(this, SettingResetActivity::class.java)
//                startActivity(intent)
//            }
        }
    }

    private fun signOut() {
        val customDialog = CustomDialog(mContext).apply {
            setBottomDialog(getString(R.string.dialog_title),
                    getString(R.string.signout_msg),
                    null,
                    getString(R.string.btnCancel), null,
                    getString(R.string.btnConfirm), View.OnClickListener {

                val mOAuthLoginInstance = OAuthLogin.getInstance()
                if (OAuthLoginState.NEED_LOGIN != mOAuthLoginInstance.getState(mContext)) {
                    mOAuthLoginInstance.logout(mContext)
                }
                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW2002)

                settingViewModel.signOut(jsonObject)
                dismiss()
            })
        }
        customDialog.show()
    }

}
