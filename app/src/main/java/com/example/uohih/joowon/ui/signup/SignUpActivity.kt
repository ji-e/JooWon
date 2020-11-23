package com.example.uohih.joowon.ui.signup

import android.content.Intent
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
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.ui.signin.SignInViewModel
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.btn_positive.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private val signUpViewModel: SignUpViewModel by viewModel()
    private lateinit var binding: ActivitySignupBinding

    private lateinit var edtPW: EditText
    private lateinit var edtPW2: EditText
    private lateinit var chkPwVisible: CheckBox
    private lateinit var chkPw2Visible: CheckBox
    private lateinit var btnSignUp: Button


    private var signInBundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySignupBinding>(thisActivity, R.layout.activity_signup)
        binding.run {
            lifecycleOwner = thisActivity
            signUpVm = signUpViewModel
        }

        val args = intent
        if (args.hasExtra("signIn")) {
            signInBundle = args.getBundleExtra("signIn")
        }

        initView()
    }


    private fun initView() {

        edtPW = binding.signupEdtPw
        edtPW2 = binding.signupEdtPw2
        chkPwVisible = binding.signupCkbPwVisible
        chkPw2Visible = binding.signupCkbPw2Visible
        btnSignUp = binding.signupBtnSignUp.btnPositive


        btnSignUp.text = getString(R.string.signin_sign_up)
        btnSignUp.setOnClickListener {
            // 가입하기
            if (binding.signupBtnSignUp.isEnabled) {
                signUp()
            }
        }

        edtPW.addTextChangedListener(SignUpTextWatcher(edtPW))
        edtPW2.addTextChangedListener(SignUpTextWatcher(edtPW2))


        edtPW.onFocusChangeListener = SignUpFocusChangeListner()
        edtPW2.onFocusChangeListener = SignUpFocusChangeListner()

        edtPW2.setOnEditorActionListener(SignUpEditActionListener())

        chkPwVisible.setOnCheckedChangeListener(SignUpCheckChangeListener())
        chkPw2Visible.setOnCheckedChangeListener(SignUpCheckChangeListener())


        setObserve()
    }

    private fun setObserve() {
        with(signUpViewModel) {
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

            jw1002Data.observe(this@SignUpActivity, Observer {
                if ("Y" == it.resbody?.signUpValid) {
                    finish()
                }
            })

            jw2001Data.observe(this@SignUpActivity, Observer {
                if ("Y" == it.resbody?.signInValid) {
                    goMain()
                }
            })
        }
    }

    /**
     * 메인으로이동
     */
    private fun goMain() {
        val intent = Intent(this, MainListActivity::class.java).apply {
            putExtra("email", signInBundle.getString("email", ""))
        }
        startActivity(intent)
        finish()
    }


    /**
     * 회원가입
     */
    private fun signUp() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW1002)
        jsonObject.addProperty("email", signInBundle.getString("email", ""))
        jsonObject.addProperty("password", edtPW.text.toString())
        jsonObject.addProperty("provider", UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID))
        signUpViewModel.signUp(jsonObject)

        edtPW.clearFocus()
        edtPW2.clearFocus()
    }

    /**
     * 텍스트 입력 리스너
     */
    private inner class SignUpTextWatcher(private val mEditText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            if (mEditText == edtPW) {
                edtPW2.setText("")
            }

            signUpViewModel.signUpDataChanged(
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
            chkPwVisible.isChecked = false
            chkPw2Visible.isChecked = false

            if (hasFocus) {
                when (v) {
                    edtPW -> chkPwVisible.visibility = View.VISIBLE
                    edtPW2 -> chkPw2Visible.visibility = View.VISIBLE
                }
            } else {
                when (v) {
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
