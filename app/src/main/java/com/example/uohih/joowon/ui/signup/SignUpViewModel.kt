package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.model.JW1002
import com.example.uohih.joowon.model.SignUpFormState
import com.example.uohih.joowon.data.setting.SettingRepository
import com.example.uohih.joowon.data.signup.SignUpRepository
import com.example.uohih.joowon.model.JW2001
import com.example.uohih.joowon.util.LogUtil
import com.google.gson.JsonObject
import java.util.regex.Pattern


class SignUpViewModel(private val signUpRepository: SignUpRepository) : JWBaseViewModel() {
    private val _signUpForm = MutableLiveData<SignUpFormState>()
    private val _jw1002Data = MutableLiveData<JW1002>()
    private val _jw2001Data = MutableLiveData<JW2001>()

    val signUpFormState: LiveData<SignUpFormState> = _signUpForm
    val jw1002Data: LiveData<JW1002> = _jw1002Data
    val jw2001Data: LiveData<JW2001> = _jw2001Data

    private fun isDataValidCheck() {
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
        _isLoading.value = true
        signUpRepository.signUp(
                jsonObject = jsonObject,
                success = {
                    _jw1002Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e( "" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }

    /**
     * 로그인
     * jw2001
     */
    fun signIn(jsonObject: JsonObject) {
        _isLoading.value = true
        signUpRepository.signIn(
                jsonObject = jsonObject,
                success = {
                    _jw2001Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }
}