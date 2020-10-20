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
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.databinding.ActivitySigninBinding
import com.example.uohih.joowon.ui.customView.CalendarDialog
import com.example.uohih.joowon.ui.customView.CalendarDialog.ConfirmBtnClickListener
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.ui.signup.SignUpActivity
import com.example.uohih.joowon.util.DateCommonUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SignInActivity : JWBaseActivity() {
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var binding: ActivitySigninBinding

    private val thisActivity by lazy { this }

    private val customDialog by lazy {
        CustomDialog(mContext, android.R.style.Theme_Material_Dialog_MinWidth)
    }


    private lateinit var mOAuthLoginInstance: OAuthLogin

    private lateinit var edtEmail: EditText
    private lateinit var edtPW: EditText
    private lateinit var btnEmailDelete: ImageButton
    private lateinit var chkPwVisible: CheckBox
    private lateinit var chkAutoSignIn: CheckBox

    private var isNaverSignIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySigninBinding>(thisActivity, R.layout.activity_signin)
        binding.run {
            signInViewModel = ViewModelProviders.of(thisActivity, SignInViewModelFactory()).get(SignInViewModel::class.java)
            lifecycleOwner = thisActivity
            signInVm = signInViewModel
        }


        initView()
        initNaverLoginData()


//        signInViewModel.getSignInState()
    }

    private fun initView() {
        edtEmail = binding.signinEdtEmail
        edtPW = binding.signinEdtPw
        chkPwVisible = binding.signinChkPwVisible
        btnEmailDelete = binding.signinBtnDelete
        chkAutoSignIn = binding.signinChkAutoSignIn

        edtEmail.onFocusChangeListener = SignInFocusChangeListener()
        edtPW.onFocusChangeListener = SignInFocusChangeListener()

        edtEmail.addTextChangedListener(SignInTextWatcher(edtEmail))
        edtPW.addTextChangedListener(SignInTextWatcher(edtPW))

        edtPW.setOnEditorActionListener(SignInEditActionListener())

        chkPwVisible.setOnCheckedChangeListener(SignInCheckChangeListener())

        setObserve()
    }

    private fun setObserve() {
        signInViewModel.isLoading.observe(this@SignInActivity, Observer {
            val isLoading = it ?: return@Observer

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        signInViewModel.jw1002Data.observe(this@SignInActivity, Observer {
            val jw1002Data = it ?: return@Observer
            if ("Y" == jw1002Data.resbody?.signUpValid) {
                goMain()
            }
        })

        signInViewModel.jw1005Data.observe(this@SignInActivity, Observer {
            val jw1005Data = it ?: return@Observer
            if ("Y" == jw1005Data.resbody?.adminUpdateValid) {
                goMain()
            }
        })

        signInViewModel.jw1006Data.observe(this@SignInActivity, Observer {
            val jw1006Data = it ?: return@Observer

            if ("Y" == jw1006Data.resbody?.isSnsIdRegisted) {
//                // todo 로그인완료?
//                goMain()
            } else {
                if ("Y" == jw1006Data.resbody?.isEmailRegisted) {
                    if (isNaverSignIn) {
                        // 중복임
                        customDialog.showDialog(
                                thisActivity,
                                getString(R.string.signin_dialog_msg_email_registration),
                                getString(R.string.btnCancel),
                                DialogInterface.OnClickListener { dialog, which ->
                                    goMain()
                                },
                                getString(R.string.btnConfirm),
                                DialogInterface.OnClickListener { dialog, which ->
                                    adminUpdate()
                                })
                    } else {
                        goMain()
                    }
                }
            }
        })


        signInViewModel.jw2001Data.observe(this@SignInActivity, Observer {
            val jw2001Data = it ?: return@Observer

            if ("Y" == jw2001Data.resbody?.signInValid) {
                // todo 로그인완료?
                if (chkAutoSignIn.isChecked) {
                    jw2001Data.resbody.autoToken?.let { it1 -> UICommonUtil.setPreferencesData(Constants.PREFERENCE_AUTO_SIGNIN_TOKEN, it1) }
                }
                if (signInViewModel.jw1006Data.value?.resbody?.isSnsIdRegisted == "Y" || !isNaverSignIn) goMain()
            } else {
                customDialog.showDialog(
                        thisActivity,
                        getString(R.string.signin_err),
                        getString(R.string.btnConfirm), null)
            }
        })
    }

    private val mOAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            hideLoading()
            if (success) {
                isNaverSignIn = true
                LogUtil.e(mOAuthLoginInstance.getAccessToken(thisActivity))
                signInViewModel.getSnsSignInInfo(mOAuthLoginInstance.getAccessToken(thisActivity))
            } else {
            }
        }

    }


    private fun initNaverLoginData() {
        mOAuthLoginInstance = OAuthLogin.getInstance()
        mOAuthLoginInstance.showDevelopersLog(true)
        mOAuthLoginInstance.init(this, Constants.OAUTH_CLIENT_ID, Constants.OAUTH_CLIENT_SECRET, Constants.OAUTH_CLIENT_NAME)

        if (mOAuthLoginInstance.getAccessToken(this) != null) {
            if (OAuthLoginState.NEED_LOGIN != mOAuthLoginInstance.getState(this)
                    && OAuthLoginState.NEED_INIT != mOAuthLoginInstance.getState(this)) {
                signInViewModel.getSnsSignInInfo(mOAuthLoginInstance.getAccessToken(this))
            }
        }
    }

    private fun goMain() {
        val intent = Intent(this, MainListActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onClickSignIn(view: View) {
        when (view) {
            binding.signinTvSignup -> {
                // 회원가입화면으로 이동
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            binding.signinTvFindPw -> {
                // 비밀번호 찾기
                showCalendarDialog()
            }
            binding.signinBtnSignin -> {
                // 로그인
                signIn()
            }
            binding.signinBtnOAuthLoginImg -> {
                // 네이버아이디로 로그인
//                showLoading()
                mOAuthLoginInstance.startOauthLoginActivity(thisActivity, mOAuthLoginHandler)
            }
            binding.signinBtnDelete -> {
                // 입력한 아이디 삭제
                edtEmail.setText("")
            }
        }
    }

    /**
     * 로그인
     */
    private fun signIn() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW2001)
        jsonObject.addProperty("email", edtEmail.text.toString())
        jsonObject.addProperty("password", edtPW.text.toString())
        jsonObject.addProperty("provider", UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID))
        signInViewModel.signIn(jsonObject)

        edtEmail.clearFocus()
        edtPW.clearFocus()
    }

    private fun adminUpdate() {
        val jw1003Data = signInViewModel.jw1003Data.value?.response
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW1005)
        jsonObject.addProperty("email", jw1003Data?.email)

        val updateData = JsonObject()
        updateData.addProperty("authToken", jw1003Data?.id)
        updateData.addProperty("updated_at", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))

        val snsProvider = JsonObject()
        snsProvider.addProperty("id", jw1003Data?.id)
        snsProvider.addProperty("email", jw1003Data?.email)
        snsProvider.addProperty("name", jw1003Data?.name)
        updateData.add("sns_provider", snsProvider)

        jsonObject.add("update_data", updateData)

        signInViewModel.updateAdminInfo(jsonObject)
    }

    /**
     * 포커스 체인지 리스너
     */
    private inner class SignInFocusChangeListener : View.OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hasFocus) {
                if (v == edtEmail) {
                    btnEmailDelete.visibility =
                            if (edtEmail.text.isNotEmpty()) View.VISIBLE
                            else View.GONE
                } else if (v == edtPW) {
                    chkPwVisible.visibility = View.VISIBLE
                }
            } else {
                if (v == edtEmail) {
                    btnEmailDelete.visibility = View.GONE
                } else if (v == edtPW) {
                    chkPwVisible.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 텍스트 입력 리스너
     */
    private inner class SignInTextWatcher(private val mEditText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            if (mEditText == edtEmail) {
                btnEmailDelete.visibility =
                        if (charSequence.isNotEmpty()) View.VISIBLE
                        else View.GONE
            }
        }

        override fun afterTextChanged(s: Editable) {
            signInViewModel.signInDataChanged(
                    edtEmail.text.toString(),
                    edtPW.text.toString())
        }
    }

    /**
     * 체크박스 체크 리스너
     */
    private inner class SignInCheckChangeListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
            when (view) {
                chkPwVisible -> {
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
     * 캘린더 다이얼로그
     */
    private fun showCalendarDialog() {
        val date = "2020-10-01"
        val calendarDialog = CalendarDialog(thisActivity).apply {
            setBottomDialog(
                    date,
                    null,
                    object : ConfirmBtnClickListener {
                        override fun onConfirmClick(date: LocalDate) {
                            LogUtil.e(date.month)
                        }
                    })
        }
        calendarDialog.show()

    }

}
