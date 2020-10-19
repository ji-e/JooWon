package com.example.uohih.joowon.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivitySignupBinding
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject

class SignUpActivity : JWBaseActivity() {
    private lateinit var signUpViewModel: SignUpViewModel

    private val thisActivity by lazy { this }

    private lateinit var binding: ActivitySignupBinding

    private lateinit var edtEmail: EditText
    private lateinit var edtPW: EditText
    private lateinit var edtPW2: EditText
    private lateinit var btnEmailDelete: ImageButton
    private lateinit var chkPwVisible: CheckBox
    private lateinit var chkPw2Visible: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySignupBinding>(thisActivity, R.layout.activity_signup)
        binding.run {
            signUpViewModel = ViewModelProviders.of(thisActivity, SignUpViewModelFactory()).get(SignUpViewModel::class.java)
            lifecycleOwner = thisActivity
            signUpVm = signUpViewModel
        }

        initView()
    }

    private fun initView() {
        edtEmail = binding.signupEdtEmail
        edtPW = binding.signupEdtPw
        edtPW2 = binding.signupEdtPw2
        btnEmailDelete = binding.signupBtnEmailDelete
        chkPwVisible = binding.signupChkPwVisible
        chkPw2Visible = binding.signupChkPw2Visible

        edtEmail.addTextChangedListener(SignUpTextWatcher(edtEmail))
        edtPW.addTextChangedListener(SignUpTextWatcher(edtPW))
        edtPW2.addTextChangedListener(SignUpTextWatcher(edtPW2))

        edtEmail.onFocusChangeListener = SignUpFocusChangeListner()
        edtPW.onFocusChangeListener = SignUpFocusChangeListner()
        edtPW2.onFocusChangeListener = SignUpFocusChangeListner()

        edtPW2.setOnEditorActionListener(SignUpEditActionListener())

        chkPwVisible.setOnCheckedChangeListener(SignUpCheckChangeListener())
        chkPw2Visible.setOnCheckedChangeListener(SignUpCheckChangeListener())

        setObserve()
    }

    private fun setObserve() {
        signUpViewModel.isLoading.observe(this@SignUpActivity, Observer {
            val isLoading = it ?: return@Observer

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })
        signUpViewModel.JW1002Data.observe(this@SignUpActivity, Observer {
            val jw1002Data = it ?: return@Observer

            if ("Y" == jw1002Data.resbody?.signUpValid) {
                finish()
            }
        })
    }

    fun onClickSignUp(view: View) {
        when (view) {
            binding.signupBtnSignup -> {
                // 회원가입
                signUp()
            }
            binding.signupBtnEmailConfirm -> {
                // 이메일 중복확인
                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW1001)
                jsonObject.addProperty("email", edtEmail.text.toString())
                signUpViewModel.isEmailOverlapConfirm(jsonObject)
            }
            btnEmailDelete -> {
                // 입력한 아이디 삭제
                edtEmail.setText("")
            }

        }

    }

    /**
     * 회원가입
     */
    private fun signUp() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW1002)
        jsonObject.addProperty("email", edtEmail.text.toString())
        jsonObject.addProperty("password", edtPW.text.toString())
        jsonObject.addProperty("provider", UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID))
        signUpViewModel.signUp(jsonObject)

        edtEmail.clearFocus()
        edtPW.clearFocus()
        edtPW2.clearFocus()
    }

    /**
     * 텍스트 입력 리스너
     */
    private inner class SignUpTextWatcher(private val mEditText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            if (mEditText == edtEmail) {
                btnEmailDelete.visibility =
                        if (charSequence.isNotEmpty()) View.VISIBLE
                        else View.GONE
            }
        }

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

    /**
     * 포커스 체인지 리스너
     */
    private inner class SignUpFocusChangeListner : View.OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hasFocus) {
                when (v) {
                    edtEmail -> btnEmailDelete.visibility =
                            if (edtEmail.text.isNotEmpty()) View.VISIBLE
                            else View.GONE
                    edtPW -> chkPwVisible.visibility = View.VISIBLE
                    edtPW2 -> chkPw2Visible.visibility = View.VISIBLE
                }
            } else {
                when (v) {
                    edtEmail -> btnEmailDelete.visibility = View.GONE
                    edtPW -> chkPwVisible.visibility = View.GONE
                    edtPW2 -> chkPw2Visible.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 체크박스 체크 리스너
     */
    private inner class SignUpCheckChangeListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
            when (view) {
                chkPwVisible -> {
                    edtPW.transformationMethod =
                            if (isChecked) HideReturnsTransformationMethod.getInstance()
                            else PasswordTransformationMethod.getInstance()
                    edtPW.setSelection(edtPW.text.length)
                }
                chkPw2Visible -> {
                    edtPW2.transformationMethod =
                            if (isChecked) HideReturnsTransformationMethod.getInstance()
                            else PasswordTransformationMethod.getInstance()
                    edtPW2.setSelection(edtPW2.text.length)
                }
            }
        }
    }

    /**
     * 키패드 액션리스너
     */
    private inner class SignUpEditActionListener : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signUpViewModel.signUpFormState.value?.isDataValid.let { if (it == true) signUp() }
                return false
            }
            return true

        }
    }
}
