package com.example.uohih.joowon.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.Result
import com.example.uohih.joowon.model.signup.SU1001
import com.example.uohih.joowon.model.signup.SU1002
import com.example.uohih.joowon.model.signup.SignUpFormState

import com.example.uohih.joowon.repository.signup.SignUpRepository
import com.example.uohih.joowon.repository.signup.SignUpRepository.GetResbodyCallback
import com.google.gson.JsonObject
import java.util.regex.Pattern


class SignUpViewModel(private val signUpRepository: SignUpRepository) : ViewModel() {
    private val _signUpForm = MutableLiveData<SignUpFormState>()
    private val _su1001Data = MutableLiveData<SU1001>()
    private val _su1002Data = MutableLiveData<SU1002>()

    val signUpFormState: LiveData<SignUpFormState> = _signUpForm
    val su1001Data: LiveData<SU1001> = _su1001Data
    val su1002Data: LiveData<SU1002> = _su1002Data


    fun signUp(jsonObject: JsonObject) {
        signUpRepository.su1002(jsonObject, object : GetResbodyCallback<SU1002> {

            override fun onSuccess(data: SU1002) {
//                LogUtil.d(data..result as String)
            }

            override fun onFailure(code: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(throwable: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }


    fun isDataValidCheck() {
        if (_signUpForm.value?.emailMsg == R.string.signup_email_confirm
                && _signUpForm.value?.passwordError == null
                && _signUpForm.value?.password2Error == null) {
            _signUpForm.value = SignUpFormState( emailMsg = signUpFormState.value?.emailMsg,isDataValid = true)
        }
    }

    fun signUpDataChanged(email: String, password: String, password2: String) {
        val emailError = isEmailValid(email)
        val passwordErr = isPasswordValid(password)
        val password2Err = isPassword2Valid(password, password2)

        _signUpForm.value = SignUpFormState(
                emailMsg = emailError,
                passwordError = passwordErr,
                password2Error = password2Err,
                isEmailOverlapValid = emailError==null)

        isDataValidCheck()
    }

    fun isEmailOverlapConfirm(jsonObject: JsonObject) {
        signUpRepository.su1001(jsonObject, object : GetResbodyCallback<SU1001> {
            override fun onSuccess(data: SU1001) {
                _su1001Data.value = (data)
                if ("Y" == data.resbody?.emailValid) {
                    _signUpForm.value = SignUpFormState(
                            emailMsg = R.string.signup_email_confirm,
                            passwordError = signUpFormState.value?.passwordError,
                            password2Error = signUpFormState.value?.password2Error)
                }
                isDataValidCheck()

            }

            override fun onFailure(code: Int) {
            }

            override fun onError(throwable: Throwable) {
            }
        })
    }

    /**
     * 이메일 검증
     */
    private fun isEmailValid(email: String): Int? {
        var emailError: Int? = null

        if(email == su1001Data.value?.resbody?.email){
            if ("Y" == su1001Data.value?.resbody?.emailValid) {
                emailError = R.string.signup_email_confirm
            }
        }else {
            _su1001Data.value = null
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
        if (!Pattern.compile(Constants.PASSWORD_PATTERN).matcher(password).matches()) {
            return R.string.signup_password_err2
        }
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
}