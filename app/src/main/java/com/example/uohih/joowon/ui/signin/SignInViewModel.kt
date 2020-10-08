package com.example.uohih.joowon.ui.signin

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.repository.RetrofitSharedPreferences
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.regex.Pattern

class SignInViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : AndroidViewModel(application) {

    private val _isLoding = MutableLiveData<Boolean>()
    private val _jw0000Data = MutableLiveData<JW0000>()
    private val _jw1006Data = MutableLiveData<JW1006>()
    private val _jw1002Data = MutableLiveData<JW1002>()
    private val _jw2001Data = MutableLiveData<JW2001>()
    private val _signInForm = MutableLiveData<SignInFormState>()

    val isLoading: LiveData<Boolean> = _isLoding
    val jw0000Data: LiveData<JW0000> = _jw0000Data
    val jw1006Data: LiveData<JW1006> = _jw1006Data
    val jw1002Data: LiveData<JW1002> = _jw1002Data
    val jw2001Data: LiveData<JW2001> = _jw2001Data
    val signInFormState: LiveData<SignInFormState> = _signInForm

    init {
        val d = RetrofitSharedPreferences()
        d.getddd(application)
    }

    private fun isDataValidCheck() {
        if (_signInForm.value?.emailMsg == null
                && _signInForm.value?.passwordError == null) {
            _signInForm.value = SignInFormState(
                    isDataValid = true)
        }
    }

    fun signInDataChanged(email: String, password: String) {
        val emailError = isEmailValid(email)
        val passwordErr = isPasswordValid(password)

        _signInForm.value = SignInFormState(
                emailMsg = emailError,
                passwordError = passwordErr)

        isDataValidCheck()
    }

    /**
     * 이메일 검증
     */
    private fun isEmailValid(email: String): Int? {
        var emailError: Int? = null

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
        return null
    }


    /**
     * 네이버로그인 정보 가져오기
     * jw1003
     */
    fun getSnsSignInInfo(accessToken: String) {
        _isLoding.value = true
        jwBaseRepository.requestNaverService(accessToken, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw1003Data = Gson().fromJson(data.toString(), JW1003::class.java)

                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW1006)
                jsonObject.addProperty("email", jw1003Data.response?.email)
                jsonObject.addProperty("authToken", jw1003Data.response?.id)
                jsonObject.addProperty("password", " ")


                val snsProvider = JsonObject()
                snsProvider.addProperty("id", jw1003Data.response?.id)
                snsProvider.addProperty("email", jw1003Data.response?.email)
                snsProvider.addProperty("name", jw1003Data.response?.name)

                jsonObject.add("sns_provider", snsProvider)
                getAdminInfo(jsonObject)
                _isLoding.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoding.value = false
            }

            override fun onError(throwable: Throwable) {
                LogUtil.e("err")
                _isLoding.value = false
            }

        })

    }

    /**
     * 관리자 정보 가져오기
     * jw1006
     */
    fun getAdminInfo(jsonObject: JsonObject) {
        _isLoding.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                _isLoding.value = false

                val jw1006Data = Gson().fromJson(data.toString(), JW1006::class.java)
                LogUtil.d(jw1006Data.resbody?.successYn.toString(), jw1006Data.result.toString())
                if ("false" == jw1006Data.result) {
                    return
                }
                if ("N" == jw1006Data.resbody?.successYn) {
                    jsonObject.addProperty("methodid", Constants.JW1002)
                    signUp(jsonObject)
                    return
                }

                _jw1006Data.value = jw1006Data
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoding.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoding.value = false
            }

        })
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
                if ("N" == jw1002Data.result) {
                    return
                }
                _jw1002Data.value = jw1002Data
                _isLoding.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoding.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoding.value = false
            }

        })
    }

    /**
     * 로그인
     * jw2001
     */
    fun signIn(jsonObject: JsonObject) {
        _isLoding.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw2001Data = Gson().fromJson(data.toString(), JW2001::class.java)
                if ("N" == jw2001Data.result) {
                    return
                }
                _jw2001Data.value = jw2001Data
                _isLoding.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoding.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoding.value = false
            }

        })
    }

    /**
     * 로그인 여부 확인
     */
    fun getSignInState() {
        _isLoding.value = true
        jwBaseRepository.requestSignInService(JsonObject(), object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw0000Data = Gson().fromJson(data.toString(), JW0000::class.java)
                if ("N" == jw0000Data.result) {
                    return
                }
                _jw0000Data.value = jw0000Data
                _isLoding.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoding.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoding.value = false
            }

        })
    }


}