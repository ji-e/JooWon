package com.example.uohih.joowon.ui.signin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
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
import com.example.uohih.joowon.ui.signup.SignUpActivity
import com.google.gson.JsonObject
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : JWBaseActivity() {
    private lateinit var signInViewModel: SignInViewModel

    private val thisActivity by lazy { this }

    private val customDialog by lazy {
        CustomDialog(mContext, android.R.style.Theme_Material_Dialog_MinWidth)
    }


    private lateinit var mOAuthLoginInstance: OAuthLogin

    private lateinit var edtEmail: EditText
    private lateinit var edtPW: EditText
    private lateinit var btnEmailDelete: ImageButton
    private lateinit var chkPwVisible: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySigninBinding>(this, R.layout.activity_signin)

        signInViewModel = ViewModelProviders.of(this, SignInViewModelFactory()).get(SignInViewModel::class.java)

        binding.signInVm = signInViewModel
        binding.setLifecycleOwner(this)

        initView()
        initNaverLoginData()
    }

    private fun initView() {
        edtEmail = signin_edt_email
        edtPW = signin_edt_pw
        chkPwVisible = signin_chk_pw_visible
        btnEmailDelete = signin_btn_delete

        edtEmail.onFocusChangeListener = SignInFocusChangeListner()
        edtPW.onFocusChangeListener = SignInFocusChangeListner()

        edtEmail.addTextChangedListener(SignInTextWatcher(edtEmail))
        edtPW.addTextChangedListener(SignInTextWatcher(edtPW))

        chkPwVisible.setOnCheckedChangeListener(SignInCheckChange())

        setObserve()
    }

    private fun setObserve() {
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
                            getString(R.string.btnCancel), null,
                            getString(R.string.btnConfirm),
                            DialogInterface.OnClickListener { dialog, which ->

                                //todo
                            })
                }
            }
        })


        signInViewModel.jw2001Data.observe(this@SignInActivity, Observer {
            val jw2001Data = it ?: return@Observer

            if ("Y" == jw2001Data.resbody?.signInValid) {
                // todo 로그인완료?
                goMain()
            } else {
                customDialog.showDialog(
                        thisActivity,
                        getString(R.string.signin_err),
                        getString(R.string.btnConfirm),
                        DialogInterface.OnClickListener { dialog, which ->

                            //todo
                        })
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
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.signin_tv_find_pw -> {

            }
            R.id.signin_btn_signin -> {
                // 로그인
                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW2001)
                jsonObject.addProperty("email", edtEmail.text.toString())
                jsonObject.addProperty("password", edtPW.text.toString())
                signInViewModel.signIn(jsonObject)
            }
            R.id.signin_btn_OAuthLoginImg -> {
                // 네이버아이디로 로그인
                OAuthLogin.getInstance().startOauthLoginActivity(thisActivity, mOAuthLoginHandler)
            }
            R.id.signin_btn_delete -> {
                // 입력한 아이디 삭제
                edtEmail.setText("")
            }
        }
    }

    /**
     * 포커스 체인지 리스너
     */
    private inner class SignInFocusChangeListner : View.OnFocusChangeListener {
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
    private inner class SignInCheckChange : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
            if (isChecked) {
                edtPW.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                edtPW.transformationMethod = PasswordTransformationMethod.getInstance()
            }

            edtPW.setSelection(edtPW.text.length)
        }

    }
}
