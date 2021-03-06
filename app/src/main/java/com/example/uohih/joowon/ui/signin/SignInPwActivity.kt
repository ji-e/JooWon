package com.example.uohih.joowon.ui.signin

import android.content.DialogInterface
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
import com.example.uohih.joowon.databinding.ActivitySigninPwBinding
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.util.KeyboardShowUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.btn_positive.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInPwActivity : JWBaseActivity() {
    private val signInViewModel: SignInViewModel by viewModel()
    private lateinit var binding: ActivitySigninPwBinding

    private val thisActivity by lazy { this }

    private lateinit var edtPW: EditText
    private lateinit var ckbPwVisible: CheckBox
    private lateinit var btnSignIn: Button
    private lateinit var layDummy: LinearLayout

    private var signInBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_signin_pw)
        binding.run {
            lifecycleOwner = thisActivity
            signInVm = signInViewModel
        }

        val args = intent
        if (args.hasExtra("signIn")) {
            signInBundle = args.getBundleExtra("signIn")
        }

        initView()


    }


    private fun initView() {
        edtPW = binding.signinEdtPw
        ckbPwVisible = binding.signinCkbPwVisible
        btnSignIn = binding.signinBtnSignIn.btnPositive

        btnSignIn.text = getString(R.string.signin_title)
        btnSignIn.setOnClickListener {
            if(binding.signinBtnSignIn.isEnabled){
                // 로그인
                signIn()
            }
        }

        edtPW.addTextChangedListener(SignInTextWatcher(edtPW))
        edtPW.setOnEditorActionListener(SignInEditActionListener())

        ckbPwVisible.setOnCheckedChangeListener(SignInCheckChangeListener())


        setObserve()
    }

    private fun setObserve() {
        with(signInViewModel) {
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

            jw2001Data.observe(thisActivity, Observer {
                if ("Y" == it.resbody?.signInValid) {
                    // 로그인완료
                    if (signInBundle.getBoolean("autoSignIn", false)) {
                        it.resbody.autoToken?.let { autoToken -> UICommonUtil.setPreferencesData(Constants.PREFERENCE_AUTO_SIGNIN_TOKEN, autoToken) }
                    }
                    goMain()

                } else {
                    val customDialog = CustomDialog(mContext).apply {
                        setBottomDialog(
                                getString(R.string.signin_err),
                                getString(R.string.btnConfirm), null)
                    }
                    customDialog.show()
                }
            })
        }
    }

    fun onClickSignIn(view: View) {
        edtPW.clearFocus()
        when (view) {
            binding.signinTvFindPw -> {

            }
        }

    }

    /**
     * 텍스트 입력 리스너
     */
    private inner class SignInTextWatcher(private val mEditText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            signInViewModel.isPasswordValid(
                    edtPW.text.toString())
        }
    }

    /**
     * 체크박스 체크 리스너
     */
    private inner class SignInCheckChangeListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
            when (view) {
                ckbPwVisible -> {
                    edtPW.transformationMethod =
                            if (isChecked) HideReturnsTransformationMethod.getInstance()
                            else PasswordTransformationMethod.getInstance()
                    edtPW.setSelection(edtPW.text.length)
                }
            }
        }
    }

    /**
     * 키패드 액션리스너
     */
    private inner class SignInEditActionListener : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signInViewModel.signInFormState.value?.isDataValid.let { if (it == true) signIn() }
                return false
            }
            return true

        }
    }

    /**
     * 로그인
     */
    private fun signIn() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW2001)
        jsonObject.addProperty("email", signInBundle.getString("email", ""))
        jsonObject.addProperty("password", edtPW.text.toString())
        jsonObject.addProperty("provider", UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID))
        signInViewModel.signIn(jsonObject)

        edtPW.clearFocus()
    }


    /**
     * 메인으로 이동
     */
    private fun goMain() {
        val intent = Intent(this, MainListActivity::class.java).apply {
            putExtra("email", signInBundle.getString("email", ""))
        }
        startActivity(intent)

        finish()
    }


}