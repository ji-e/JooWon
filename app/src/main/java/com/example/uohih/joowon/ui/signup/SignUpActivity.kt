package com.example.uohih.joowon.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.databinding.ActivitySignupBinding
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : JWBaseActivity() {
    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var edtEmail: EditText
    private lateinit var edtPW: EditText
    private lateinit var edtPW2: EditText
    private lateinit var btnEmailConfirm: Button

    private lateinit var cryptoPw: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySignupBinding>(this, R.layout.activity_signup)

        signUpViewModel = ViewModelProviders.of(this, SignUpViewModelFactory()).get(SignUpViewModel::class.java)
        binding.signUpVm = signUpViewModel
        binding.setLifecycleOwner(this)

        initView()
    }

    private fun initView() {
        edtEmail = signup_edt_email
        edtPW = signup_edt_pw
        edtPW2 = signup_edt_pw2
        btnEmailConfirm = signup_btn_email_confirm

        edtEmail.addTextChangedListener(SignUpTextWatcher(edtEmail))
        edtPW.addTextChangedListener(SignUpTextWatcher(edtPW))
        edtPW2.addTextChangedListener(SignUpTextWatcher(edtPW2))

        setObserve()
    }

    private fun setObserve() {
        signUpViewModel.signUpFormState.observe(this@SignUpActivity, Observer {
            val signUpFormState = it ?: return@Observer

            if (signUpFormState.cryptoPw != null) {
                cryptoPw = signUpFormState.cryptoPw
            }
        })
    }

    fun onClickSignUp(view: View) {
        when (view.id) {
            R.id.signup_btn_signup -> {
                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW1002)
                jsonObject.addProperty("email", edtEmail.text.toString())
                jsonObject.addProperty("password", cryptoPw)
                signUpViewModel.signUp(jsonObject)
            }
            R.id.signup_btn_email_confirm -> {
                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW1001)
                jsonObject.addProperty("email", edtEmail.text.toString())
                signUpViewModel.isEmailOverlapConfirm(jsonObject)
            }

        }

    }

    /**
     * SignUpTextWatcher
     */
    private inner class SignUpTextWatcher(private val mEditText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            if (mEditText == edtPW) {
                edtPW2.setText("")
            }

            signUpViewModel.signUpDataChanged(
                    edtEmail.text.toString(),
                    edtPW.text.toString(),
                    edtPW2.text.toString()
            )
        }
    }

}
