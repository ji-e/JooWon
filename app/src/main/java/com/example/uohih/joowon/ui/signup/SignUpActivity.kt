package com.example.uohih.joowon.ui.signup

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivitySignupBinding
import kotlinx.android.synthetic.main.activity_login.*

import androidx.lifecycle.Observer

class SignUpActivity : JWBaseActivity() {
    private lateinit var signUpVidwModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_singup)

        val binding = DataBindingUtil.setContentView<ActivitySignupBinding>(this, R.layout.activity_signup)

        signUpVidwModel = ViewModelProviders.of(this, SignUpViewModelFactory())
                .get(SignUpViewModel::class.java)

        binding.signUpVm = signUpVidwModel

        binding.setLifecycleOwner(this)

        signUpVidwModel.signUpFormState.observe(this@SignUpActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })


    }
}
