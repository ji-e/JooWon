package com.example.uohih.joowon.ui.signup

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.databinding.ActivitySignUpInitActiviyBinding
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import kotlinx.android.synthetic.main.activity_sign_up_init_activiy.*

class SignUpInitActiviy : JWBaseActivity() {
    private lateinit var signUpInitViewModel: SignUpInitViewModel
    private val thisActivity by lazy { this }

    private lateinit var mOAuthLoginInstance: OAuthLogin
    private lateinit var mOAuthLoginButton: OAuthLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up_init_activiy)

        val binding = DataBindingUtil.setContentView<ActivitySignUpInitActiviyBinding>(this, R.layout.activity_sign_up_init_activiy)

        signUpInitViewModel = ViewModelProviders.of(this, SignUpInitViewModelFactory()).get(SignUpInitViewModel::class.java)

        binding.signUpInitVm = signUpInitViewModel
        binding.setLifecycleOwner(this)

        initData()
        initView()
    }

    private fun initView() {
        mOAuthLoginButton = buttonOAuthLoginImg
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler)


        signUpInitViewModel.JW1002Data.observe(this@SignUpInitActiviy, Observer {
            val su1002Data = it ?: return@Observer
            if("Y"==su1002Data.resbody?.successYN){
                finish()
            }
        })


    }
    private val mOAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            LogUtil.e(mOAuthLoginInstance.getAccessToken(thisActivity))
            if (success) {
//                signUpInitViewModel.su1003(mOAuthLoginInstance.getAccessToken(thisActivity))
            } else {
            }
        }

    }


    fun initData(){
        mOAuthLoginInstance = OAuthLogin.getInstance()
//
        mOAuthLoginInstance.showDevelopersLog(true)
        mOAuthLoginInstance.init(this, Constants.OAUTH_CLIENT_ID, Constants.OAUTH_CLIENT_SECRET, Constants.OAUTH_CLIENT_NAME)

    }

}
