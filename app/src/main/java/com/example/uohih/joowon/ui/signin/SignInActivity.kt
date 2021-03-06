package com.example.uohih.joowon.ui.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.databinding.ActivitySigninBinding
import com.example.uohih.joowon.ui.customView.CalendarDialog
import com.example.uohih.joowon.ui.customView.CalendarDialog.ConfirmBtnClickListener
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.ui.signup.SignUpActivity
import com.example.uohih.joowon.util.KeyboardShowUtil
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import kotlinx.android.synthetic.main.btn_positive.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SignInActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private val signInViewModel: SignInViewModel by viewModel()
    private lateinit var binding: ActivitySigninBinding

    private lateinit var keyboardShowUtil: KeyboardShowUtil
    private lateinit var mOAuthLoginInstance: OAuthLogin

    private lateinit var edtEmail: EditText
    private lateinit var btnEmailDelete: ImageButton
    private lateinit var ckbAutoSignIn: CheckBox
    private lateinit var btnContinue: Button

    private var isNaverSignIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_signin)
        binding.run {
            lifecycleOwner = thisActivity
            signInVm = signInViewModel
        }

        initView()
        initNaverLoginData()

    }

    private fun initView() {
        edtEmail = binding.signinEdtEmail
        btnEmailDelete = binding.signinBtnDelete
        ckbAutoSignIn = binding.signinCkbAutoSignIn
        btnContinue = binding.signinBtnContinue.btnPositive


        edtEmail.addTextChangedListener(SignInTextWatcher(edtEmail))
        edtEmail.setOnEditorActionListener(SignInEditActionListener())

        btnContinue.text = getString(R.string.signin_btn_continue)
        btnContinue.setOnClickListener {
            // 계속하기
            if (binding.signinBtnContinue.isEnabled)
                continueSignIn()
        }

//        keyboardShowUtil = KeyboardShowUtil(window,
//                onShowKeyboard = {
//
//                    btnContinue.visibility = View.GONE
//                    layDummy.visibility = View.GONE
//                    layDummy.post {
//                        btnContinue.visibility = View.VISIBLE
//
//                    }
//                    btnEmailDelete.visibility =
//                            if (edtEmail.text.isNotEmpty()) View.VISIBLE
//                            else View.GONE
//                },
//                onHideKeyboard = {
////                    layDummy.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeConverterUtil(mContext).dp(230f))
//                    layDummy.post {
//                        layDummy.visibility = View.VISIBLE
//                    }
//                })

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

            jw1001Data.observe(thisActivity, Observer {
                if ("N" == it.resbody?.emailValid) {
                    // 회원가입
                    val customDialog = CustomDialog(mContext).apply {
                        setBottomDialog(
                                getString(R.string.dialog_title),
                                getString(R.string.signin_dialog_msg_email_registration2),
                                null,
                                getString(R.string.btnCancel), null,
                                getString(R.string.btnConfirm),
                                View.OnClickListener {
                                    val intentUp = Intent(mContext, SignUpActivity::class.java).apply {
                                        val bundle = Bundle()
                                        bundle.putString("email", edtEmail.text.toString())
                                        bundle.putBoolean("autoSignIn", ckbAutoSignIn.isChecked)
                                        putExtra("signIn", bundle)
                                    }
                                    mContext.startActivity(intentUp)
                                    dismiss()
                                })
                    }
                    customDialog.show()
                } else {
                    // 로그인
                    // 비밀번호 입력

                    val intentPw = Intent(mContext, SignInPwActivity::class.java).apply {
                        val bundle = Bundle()
                        bundle.putString("email", edtEmail.text.toString())
                        bundle.putBoolean("autoSignIn", ckbAutoSignIn.isChecked)
                        putExtra("signIn", bundle)
                    }
                    mContext.startActivity(intentPw)
                }
            })

            jw1002Data.observe(thisActivity, Observer {
                if ("Y" == it.resbody?.signUpValid) {
                    goMain()
                }
            })

            jw1005Data.observe(thisActivity, Observer {
                if ("Y" == it.resbody?.adminUpdateValid) {
                    goMain()
                }
            })

            jw1006Data.observe(thisActivity, Observer {
                if ("Y" == it.resbody?.isSnsIdRegisted) {

                } else {
                    if ("Y" == it.resbody?.isEmailRegisted) {
                        if (isNaverSignIn) {
                            // 중복임
                            val customDialog = CustomDialog(mContext).apply {
                                setBottomDialog(
                                        getString(R.string.signin_dialog_msg_email_registration),
                                        getString(R.string.btnCancel),
                                        View.OnClickListener {
                                            goMain()
                                            dismiss()
                                        },
                                        getString(R.string.btnConfirm),
                                        View.OnClickListener {
                                            adminUpdate()
                                            dismiss()
                                        })
                            }
                            customDialog.show()
                        } else {
                            goMain()
                        }
                    }
                }
            })

            jw2001Data.observe(thisActivity, Observer {
                if ("Y" == it.resbody?.signInValid) {
                    // 로그인완료
                    if (ckbAutoSignIn.isChecked) {
                        it.resbody.autoToken?.let { autoToken -> UICommonUtil.setPreferencesData(Constants.PREFERENCE_AUTO_SIGNIN_TOKEN, autoToken) }
                    }
                    if (signInViewModel.jw1006Data.value?.resbody?.isSnsIdRegisted == "Y" || !isNaverSignIn) goMain()
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


    fun onClickSignIn(view: View) {
        edtEmail.clearFocus()
        btnEmailDelete.visibility = View.GONE
        when (view) {
            binding.signinBtnOAuthLoginImg -> {
                // 네이버아이디로 로그인
                mOAuthLoginInstance.startOauthLoginActivity(thisActivity, mOAuthLoginHandler)
            }
            btnEmailDelete -> {
                // 입력한 아이디 삭제
                edtEmail.setText("")
            }
            binding.signinTvAutoSignIn -> {
                // 자동로그인
                ckbAutoSignIn.isChecked = !ckbAutoSignIn.isChecked
            }
        }
    }

    /**
     * 메인으로이동
     */
    private fun goMain() {
        val intent = Intent(this, MainListActivity::class.java).apply {
            putExtra("email", edtEmail.text.toString())
        }
        startActivity(intent)
        finish()
    }

    /**
     * 계속하기
     */
    private fun continueSignIn() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW1001)
        jsonObject.addProperty("email", edtEmail.text.toString())

        edtEmail.clearFocus()
        signInViewModel.isEmailOverlapConfirm(jsonObject)
    }


    /**
     * 회원업데이트
     */
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
                btnEmailDelete.visibility =
                        if (edtEmail.text.isNotEmpty()) View.VISIBLE
                        else View.GONE
            } else {
//                if (v == edtEmail) {
                btnEmailDelete.visibility = View.GONE
//                } else if (v == edtPW) {
//                    chkPwVisible.visibility = View.GONE
//                }
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
                edtEmail.requestFocus()
                btnEmailDelete.visibility =
                        if (charSequence.isNotEmpty()) View.VISIBLE
                        else View.GONE
            }
        }

        override fun afterTextChanged(s: Editable) {
            signInViewModel.isEmailValid(
                    edtEmail.text.toString())
        }
    }


    /**
     * 키패드 액션리스너
     */
    private inner class SignInEditActionListener : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signInViewModel.signInFormState.value?.isDataValid.let { if (it == true) continueSignIn() }
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
                        override fun onConfirmClick(date: ArrayList<LocalDate>) {
                            LogUtil.e(date)
                        }
                    },
                    isFutureSelect = false,
                    isSelectedMulti = false,
                    isSelectedRang = true)
        }
        calendarDialog.show()

    }

}
