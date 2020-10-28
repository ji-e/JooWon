package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.model.JW1001
import com.example.uohih.joowon.model.JW1002
import com.example.uohih.joowon.model.SignUpFormState
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.regex.Pattern


class SignUpViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : AndroidViewModel(application) {

    private val _isLoding = MutableLiveData<Boolean>()
    private val _signUpForm = MutableLiveData<SignUpFormState>()
    private val _jw1001Data = MutableLiveData<JW1001>()
    private val _jw1002Data = MutableLiveData<JW1002>()

    val isLoading: LiveData<Boolean> = _isLoding
    val signUpFormState: LiveData<SignUpFormState> = _signUpForm
    val JW1001Data: LiveData<JW1001> = _jw1001Data
    val JW1002Data: LiveData<JW1002> = _jw1002Data

    fun isDataValidCheck() {
        if (_signUpForm.value?.passwordError == null && _signUpForm.value?.password2Error == null) {
            _signUpForm.value = SignUpFormState(isDataValid = true)
        }
    }

    fun signUpDataChanged( password: String, password2: String) {

        val passwordErr = isPasswordValid(password)
        val password2Err = isPassword2Valid(password, password2)

        _signUpForm.value = SignUpFormState(
                passwordError = passwordErr,
                password2Error = password2Err)

        isDataValidCheck()
    }

    fun isEmailOverlapConfirm(jsonObject: JsonObject) {
        _isLoding.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw1001Data = Gson().fromJson(data.toString(), JW1001::class.java)
                _jw1001Data.value = jw1001Data
                if ("N" == jw1001Data.resbody?.emailValid) {
                    _signUpForm.value = SignUpFormState(
                            emailMsg = R.string.signup_email_confirm,
                            passwordError = signUpFormState.value?.passwordError,
                            password2Error = signUpFormState.value?.password2Error)
                }
                isDataValidCheck()
                _isLoding.value = false
            }

            override fun onFailure(code: Int) {
                _isLoding.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoding.value = false
            }
        })
    }

    /**
     * 이메일 검증
     */
    private fun isEmailValid(email: String): Int? {
        var emailError: Int? = null

        if (email == JW1001Data.value?.resbody?.email) {
            if ("N" == JW1001Data.value?.resbody?.emailValid) {
                emailError = R.string.signup_email_confirm
            }
        } else {
            _jw1001Data.value = null
        }

        if (!Pattern.matches(Constants.EMAIL_PATTERN, email)) {
            emailError = R.string.signup_email_err
        }
        return emailError
    }

    /**
     * 비밀번호 검증
     */
    private fun isPasswordValid(password: String): Int? {

        if (password.isEmpty()) {
            return R.string.blank
        }
        if (password.length < 8) {
            return R.string.signup_password_err1
        }
//        if (!Pattern.compile(Constants.PASSWORD_PATTERN).matcher(password).matches()) {
//            return R.string.signup_password_err2
//        }
        if (Pattern.compile(Constants.PASSWORD_CONTINUE_PATTEN).matcher(password).find()) {
            return R.string.signup_password_err3
        }
        return null
    }

    /**
     * 비밀번호재입력 검증
     */
    private fun isPassword2Valid(password: String, password2: String): Int? {
        if (password2.isEmpty()) {
            return R.string.blank
        }
        if (password != password2) {
            return R.string.signup_password_err4
        }
        return null
    }

    /**
     * 회원가입
     * jw1002
     */
    fun signUp(jsonObject: JsonObject) {
        _isLoding.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {

            override fun onSuccess(code: Int, data: JSONObject) {

                val jw1002Data = Gson().fromJson(data.toString(), JW1002::class.java)
                _jw1002Data.value = jw1002Data
                _isLoding.value = false
            }

            override fun onFailure(code: Int) {
                _isLoding.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoding.value = false
            }


        })
    }
}