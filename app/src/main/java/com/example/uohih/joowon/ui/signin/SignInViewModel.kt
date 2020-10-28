package com.example.uohih.joowon.ui.signin

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.regex.Pattern

class SignInViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : AndroidViewModel(application) {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _jw1001Data = MutableLiveData<JW1001>()
    private val _jw1002Data = MutableLiveData<JW1002>()
    private val _jw1003Data = MutableLiveData<JW1003>()
    private val _jw1005Data = MutableLiveData<JW1005>()
    private val _jw1006Data = MutableLiveData<JW1006>()
    private val _jw2001Data = MutableLiveData<JW2001>()
    private val _signInForm = MutableLiveData<SignInFormState>()

    val isLoading: LiveData<Boolean> = _isLoading
    val jw1001Data: LiveData<JW1001> = _jw1001Data  // 아이디확인
    val jw1002Data: LiveData<JW1002> = _jw1002Data  // 회원가입
    val jw1003Data: LiveData<JW1003> = _jw1003Data  // 네이버로그인정보가져오기
    val jw1005Data: LiveData<JW1005> = _jw1005Data  // 회원정보업데이트
    val jw1006Data: LiveData<JW1006> = _jw1006Data  // 회원정보가져오기
    val jw2001Data: LiveData<JW2001> = _jw2001Data  // 로그인
    val signInFormState: LiveData<SignInFormState> = _signInForm

    private fun isDataValidCheck() {
        if (_signInForm.value?.emailMsg == null
                && _signInForm.value?.passwordError == null) {
            _signInForm.value = SignInFormState(
                    isDataValid = true)
        }
    }

    fun signInDataChanged(email: String) {
        var emailMsg: Int? = null
        var isDataValid = true


        if (!Pattern.matches(Constants.EMAIL_PATTERN, email)) {
            emailMsg = R.string.signup_email_err
            isDataValid = false
        }

        _signInForm.value = SignInFormState(
                emailMsg = emailMsg,
                isDataValid = isDataValid)


//        isDataValidCheck()
    }

    /**
     * 이메일 검증
     */
    fun isEmailValid(email: String) {
        var emailMsg: Int? = null
        var isDataValid = false

        if (email.isNotEmpty() && !Pattern.matches(Constants.EMAIL_PATTERN, email)) {
            emailMsg = R.string.signup_email_err
        } else {
            isDataValid = true
        }

        _signInForm.value = SignInFormState(
                emailMsg = emailMsg,
                isDataValid = isDataValid)

    }

    /**
     * 비밀번호 검증
     */
    fun isPasswordValid(password: String) {
        var passwordError: Int? = null
        var isDataValid = false

        if (password.length in 1..7) {
            passwordError = R.string.signup_password_err1
        } else {
            isDataValid = true
        }

        _signInForm.value = SignInFormState(
                passwordError = passwordError,
                isDataValid = isDataValid)
    }


    /**
     * 아이디 확인
     * jw1001
     */
    fun isEmailOverlapConfirm(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw1001Data = Gson().fromJson(data.toString(), JW1001::class.java)
                _jw1001Data.value = jw1001Data
                _isLoading.value = false
            }

            override fun onFailure(code: Int) {
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
            }
        })
    }

    /**
     * 회원가입
     * jw1002
     */
    fun signUp(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw1002Data = Gson().fromJson(data.toString(), JW1002::class.java)
                if ("N" == jw1002Data.result) {
                    return
                }
                _jw1002Data.value = jw1002Data
                _isLoading.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
            }

        })
    }

    /**
     * 네이버로그인 정보 가져오기
     * jw1003
     */
    fun getSnsSignInInfo(accessToken: String) {
        _isLoading.value = true
        jwBaseRepository.requestNaverService(accessToken, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                _isLoading.value = false

                val jw1003Data = Gson().fromJson(data.toString(), JW1003::class.java)

                _jw1003Data.value = jw1003Data

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

            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                LogUtil.e("err")
                _isLoading.value = false
            }

        })

    }

    /**
     * 회원정보 업데이트
     * jw1005
     */
    fun updateAdminInfo(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw1005Data = Gson().fromJson(data.toString(), JW1005::class.java)
                if ("N" == jw1005Data.result) {
                    return
                }
                _jw1005Data.value = jw1005Data
                _isLoading.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
            }

        })
    }

    /**
     * 관리자 정보 가져오기
     * jw1006
     */
    fun getAdminInfo(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                _isLoading.value = false

                val jw1006Data = Gson().fromJson(data.toString(), JW1006::class.java)

                if ("false" == jw1006Data.result) {
                    return
                }
                if ("N" == jw1006Data.resbody?.successYn) {
                    jsonObject.addProperty("methodid", Constants.JW1002)
                    jsonObject.addProperty("provider", UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID))
                    signUp(jsonObject)
                    return
                } else {
                    // 로그인
                    jsonObject.addProperty("methodid", Constants.JW2001)
                    jsonObject.addProperty("naverSignIn", "Y")
                    signIn(jsonObject)
                }

                _jw1006Data.value = jw1006Data
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
            }

        })
    }


    /**
     * 로그인
     * jw2001
     */
    fun signIn(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestSignInService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw2001Data = Gson().fromJson(data.toString(), JW2001::class.java)
//                if ("N" == jw2001Data.result) {
//                    return
//                }
                _jw2001Data.value = jw2001Data
                _isLoading.value = false
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
                _jw2001Data.value = JW2001(resbody = null)
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                _jw2001Data.value = JW2001(resbody = null)
                _isLoading.value = false
            }

        })
    }


}