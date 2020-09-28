package com.example.uohih.joowon.ui.signin

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.customview.CustomDialog
import com.example.uohih.joowon.databinding.ActivitySigninBinding
import com.example.uohih.joowon.ui.vacation.VacationActivity
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : JWBaseActivity() {
    private lateinit var signInViewModel: SignInViewModel

    private val thisActivity by lazy { this }

    private val customDialog by lazy {
        CustomDialog(mContext, android.R.style.Theme_Material_Dialog_MinWidth)
    }


    private lateinit var mOAuthLoginInstance: OAuthLogin
    private lateinit var mOAuthLoginButton: OAuthLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signin)

        val binding = DataBindingUtil.setContentView<ActivitySigninBinding>(this, R.layout.activity_signin)

        signInViewModel = ViewModelProviders.of(this, SignInViewModelFactory()).get(SignInViewModel::class.java)

        binding.signInVm = signInViewModel
        binding.setLifecycleOwner(this)

        initView()
        initNaverLoginData()
    }

    private fun initView() {
        mOAuthLoginButton = signin_btn_OAuthLoginImg
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler)

//        signInViewModel.jw1002Data.observe(this@SignInActivity, Observer {
//            val jw1002Data = it ?: return@Observer
//            if("Y"==jw1002Data.resbody?.successYN){
//                finish()
//            }
//        })


        signInViewModel.jw1006Data.observe(this@SignInActivity, Observer {
            val jw1006Data = it ?: return@Observer

            if ("Y" == jw1006Data.resbody?.isSnsIdRegisted) {
                // todo 로그인완료?

            } else {
                if ("Y" == jw1006Data.resbody?.isEmailRegisted) {
                    // 중복임
                    customDialog.showDialog(
                            thisActivity,
                            getString(R.string.signin_dialog_msg),
                            getString(R.string.btn01),
                            DialogInterface.OnClickListener { dialog, which ->

                                //todo
                            },
                            getString(R.string.btn02), null)
                }
            }
        })


    }

    private val mOAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            LogUtil.e(mOAuthLoginInstance.getAccessToken(thisActivity))
            if (success) {
                signInViewModel.getSnsSignInInfo(mOAuthLoginInstance.getAccessToken(thisActivity))
            } else {
            }
        }

    }


    private fun initNaverLoginData() {
        mOAuthLoginInstance = OAuthLogin.getInstance()
//
        mOAuthLoginInstance.showDevelopersLog(true)
        mOAuthLoginInstance.init(this, Constants.OAUTH_CLIENT_ID, Constants.OAUTH_CLIENT_SECRET, Constants.OAUTH_CLIENT_NAME)

    }
}
