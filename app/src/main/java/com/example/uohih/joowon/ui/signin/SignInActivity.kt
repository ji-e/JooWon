package com.example.uohih.joowon.ui.signin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.databinding.ActivitySigninBinding
import com.example.uohih.joowon.ui.customView.CalendarDialog
import com.example.uohih.joowon.ui.customView.CalendarDialog.ConfirmBtnClickListener
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.ui.signup.SignUpActivity
import com.example.uohih.joowon.util.KeyboardShowUtil
import com.example.uohih.joowon.util.SizeConverterUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.data.OAuthLoginState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SignInActivity : JWBaseActivity() {
    val thisActivity by lazy { this }

    private lateinit var signInViewModel: SignInViewModel
    private lateinit var binding: ActivitySigninBinding

    private val customDialog by lazy {
        CustomDialog(mContext, android.R.style.Theme_Material_Dialog_MinWidth)
    }

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
            signInViewModel = ViewModelProviders.of(thisActivity, SignInViewModelFactory()).get(SignInViewModel::class.java)
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
        btnContinue = binding.signinBtnContinue

//        edtEmail.onFocusChangeListener = SignInFocusChangeListener()
        edtEmail.addTextChangedListener(SignInTextWatcher(edtEmail))
        edtEmail.setOnEditorActionListener(SignInEditActionListener())

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
        signInViewModel.isLoading.observe(thisActivity, Observer {
            val isLoading = it ?: return@Observer

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        signInViewModel.jw1001Data.observe(thisActivity, Observer {
            val jw1001Data = it ?: return@Observer
            if ("N" == jw1001Data.resbody?.emailValid) {
                // 회원가입
                customDialog.showDialog(
                        thisActivity,
                        getString(R.string.signin_dialog_msg_email_registration2),
                        getString(R.string.btnCancel), null,
                        getString(R.string.btnConfirm),
                        DialogInterface.OnClickListener { dialog, which ->
                            val intentUp = Intent(mContext, SignUpActivity::class.java).apply {
                                val bundle = Bundle()
                                bundle.putString("email", edtEmail.text.toString())
                                bundle.putBoolean("autoSignIn", ckbAutoSignIn.isChecked)
                                putExtra("signIn", bundle)
                            }
                            mContext.startActivity(intentUp)
                        })
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

        signInViewModel.jw1002Data.observe(thisActivity, Observer {
            val jw1002Data = it ?: return@Observer
            if ("Y" == jw1002Data.resbody?.signUpValid) {
                goMain()
            }
        })

        signInViewModel.jw1005Data.observe(thisActivity, Observer {
            val jw1005Data = it ?: return@Observer
            if ("Y" == jw1005Data.resbody?.adminUpdateValid) {
                goMain()
            }
        })

        signInViewModel.jw1006Data.observe(thisActivity, Observer {
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

        signInViewModel.jw2001Data.observe(thisActivity, Observer {
            val jw2001Data = it ?: return@Observer
            if (jw2001Data.resbody == null) {
                customDialog.showDialog(
                        thisActivity,
                        getString(R.string.network_Err),
                        getString(R.string.btnConfirm), DialogInterface.OnClickListener { dialog, which ->
                    exit()
                })
                return@Observer
            }
            if ("Y" == jw2001Data.resbody.signInValid) {
                // todo 로그인완료?
                if (ckbAutoSignIn.isChecked) {
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


    fun onClickSignIn(view: View) {
        edtEmail.clearFocus()
        btnEmailDelete.visibility = View.GONE
        when (view) {
            btnContinue -> {
                // 계속하기
                continueSignIn()
            }
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
        val intent = Intent(this, MainListActivity::class.java)
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
