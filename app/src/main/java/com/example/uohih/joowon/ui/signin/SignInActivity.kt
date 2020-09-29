package com.example.uohih.joowon.ui.signin

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.customview.CustomDialog
import com.example.uohih.joowon.databinding.ActivitySigninBinding
import com.example.uohih.joowon.ui.main.MainListActivity
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
    private lateinit var mOAuthLoginButton: ImageButton

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
//        mOAuthLoginButton = signin_btn_OAuthLoginImg
//        mOAuthLoginButton.setOnClickListener { OAuthLogin.getInstance().startOauthLoginActivity(mContext as Activity, mOAuthLoginHandler) }
//        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler)
//        mOAuthLoginButton.setBgResourceId(R.drawable.btn_naver_signin)

        signInViewModel.jw1002Data.observe(this@SignInActivity, Observer {
            val jw1002Data = it ?: return@Observer
            if ("Y" == jw1002Data.resbody?.successYn) {
                goMain()
            }
        })


        signInViewModel.jw1006Data.observe(this@SignInActivity, Observer {
            val jw1006Data = it ?: return@Observer

            if ("Y" == jw1006Data.resbody?.isSnsIdRegisted) {
                // todo 로그인완료?
                goMain()
            } else {
                if ("Y" == jw1006Data.resbody?.isEmailRegisted) {
                    // 중복임
                    customDialog.showDialog(
                            thisActivity,
                            getString(R.string.signin_dialog_msg_email_registration),
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
        mOAuthLoginInstance.showDevelopersLog(true)
        mOAuthLoginInstance.init(this, Constants.OAUTH_CLIENT_ID, Constants.OAUTH_CLIENT_SECRET, Constants.OAUTH_CLIENT_NAME)

    }

    private fun goMain() {
        val intent = Intent(this, MainListActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onClickSignIn(view: View) {
        when (view.id) {
            R.id.signin_tv_signup -> {
                // 회원가입화면으로 이동
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.signin_tv_find_pw -> {

            }
            R.id.signin_btn_OAuthLoginImg -> {
                // 네이버아이디로 로그인
                OAuthLogin.getInstance().startOauthLoginActivity(thisActivity, mOAuthLoginHandler)
            }
        }

    }
}
