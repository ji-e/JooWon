package com.example.uohih.joowon.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.data.signin.SignInRepository
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import java.util.regex.Pattern

class SignInViewModel(private val signInRepository: SignInRepository) : JWBaseViewModel() {
    private val _jw1001Data = MutableLiveData<JW1001>()
    private val _jw1002Data = MutableLiveData<JW1002>()
    private val _jw1003Data = MutableLiveData<JW1003>()
    private val _jw1005Data = MutableLiveData<JW1005>()
    private val _jw1006Data = MutableLiveData<JW1006>()
    private val _jw2001Data = MutableLiveData<JW2001>()
    private val _signInFormState = MutableLiveData<SignInFormState>()

    val jw1001Data: LiveData<JW1001> = _jw1001Data  // 아이디확인
    val jw1002Data: LiveData<JW1002> = _jw1002Data  // 회원가입
    val jw1003Data: LiveData<JW1003> = _jw1003Data  // 네이버로그인정보가져오기
    val jw1005Data: LiveData<JW1005> = _jw1005Data  // 회원정보업데이트
    val jw1006Data: LiveData<JW1006> = _jw1006Data  // 회원정보가져오기
    val jw2001Data: LiveData<JW2001> = _jw2001Data  // 로그인
    val signInFormState: LiveData<SignInFormState> = _signInFormState

    /**
     * 이메일 검증
     */
    fun isEmailValid(email: String) {
        var emailMsg: Int? = null
        var isDataValid = false

        if (!Pattern.matches(Constants.EMAIL_PATTERN, email)) {
            emailMsg = R.string.signup_email_err
        } else if (email.isNotEmpty()) {
            isDataValid = true
        }

        _signInFormState.value = SignInFormState(
                emailMsg = emailMsg,
                isDataValid = isDataValid)

    }

    /**
     * 비밀번호 검증
     */
    fun isPasswordValid(password: String) {
        var passwordError: Int? = null
        var isDataValid = false

        if (password.length in 0..7) {
            passwordError = R.string.signup_password_err1
        } else {
            isDataValid = true
        }

        _signInFormState.value = SignInFormState(
                passwordError = passwordError,
                isDataValid = isDataValid)
    }


    /**
     * 아이디 확인
     * jw1001
     */
    fun isEmailOverlapConfirm(jsonObject: JsonObject) {
        _isLoading.value = true
        signInRepository.isEmailOverlapConfirm(
                jsonObject = jsonObject,
                success = {
                    _jw1001Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }

    /**
     * 회원가입
     * jw1002
     */
    fun signUp(jsonObject: JsonObject) {
        _isLoading.value = true
        signInRepository.signUp(
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
     * 네이버로그인 정보 가져오기
     * jw1003
     */
    fun getSnsSignInInfo(accessToken: String) {
        _isLoading.value = true
        signInRepository.getSnsSignInInfo(
                accessToken = accessToken,
                success = {
                    _jw1003Data.value = it
                    _isLoading.value = false

                    val jsonObject = JsonObject()
                    jsonObject.addProperty("methodid", Constants.JW1006)
                    jsonObject.addProperty("email", it.response?.email)
                    jsonObject.addProperty("authToken", it.response?.id)
                    jsonObject.addProperty("password", " ")

                    val snsProvider = JsonObject()
                    snsProvider.addProperty("id", it.response?.id)
                    snsProvider.addProperty("email", it.response?.email)
                    snsProvider.addProperty("name", it.response?.name)

                    jsonObject.add("sns_provider", snsProvider)
                    getAdminInfo(jsonObject)

                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }

    /**
     * 회원정보 업데이트
     * jw1005
     */
    fun updateAdminInfo(jsonObject: JsonObject) {
        _isLoading.value = true
        signInRepository.updateAdminInfo(
                jsonObject = jsonObject,
                success = {
                    _jw1005Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }

    /**
     * 관리자 정보 가져오기
     * jw1006
     */
    fun getAdminInfo(jsonObject: JsonObject) {
        _isLoading.value = true
        signInRepository.getAdminInfo(
                jsonObject = jsonObject,
                success = {
                    _isLoading.value = false
                    if ("N" == it.resbody?.successYn) {
                        jsonObject.addProperty("methodid", Constants.JW1002)
                        jsonObject.addProperty("provider", UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID))
                        signUp(jsonObject)
                        return@getAdminInfo
                    } else {
                        // 로그인
                        jsonObject.addProperty("methodid", Constants.JW2001)
                        jsonObject.addProperty("naverSignIn", "Y")
                        signIn(jsonObject)
                    }
                    _jw1006Data.value = it
                },
                failure = {
                    LogUtil.e("" + it)
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
        signInRepository.signIn(
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