package com.example.uohih.joowon.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.repository.signin.SignInRepository
import com.example.uohih.joowon.repository.signup.SignUpDataSource
import com.example.uohih.joowon.repository.signup.SignUpRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

class SignInViewModel(private val signInRepository: SignInRepository) : ViewModel() {
    private val signUpRepository = SignUpRepository(SignUpDataSource())
    private val gson = Gson()

    private val _jw1006Data = MutableLiveData<JW1006>()
    val jw1006Data: LiveData<JW1006> = _jw1006Data

    private val _jw1002Data = MutableLiveData<JW1002>()
    val jw1002Data: LiveData<JW1002> = _jw1002Data

    /**
     * 네이버로그인 정보 가져오기
     * jw1003
     */
    fun getSnsSignInInfo(accessToken: String) {
        signInRepository.jw1003(accessToken, object : SignInRepository.GetResbodyCallback<JSONObject> {
            override fun onSuccess(data: JSONObject) {
                val jw1003Data = gson.fromJson(data.toString(), JW1003::class.java)

                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW1006)
                jsonObject.addProperty("email", jw1003Data.response?.email)
                jsonObject.addProperty("sns_id", jw1003Data.response?.id)
                jsonObject.addProperty("sns_provider", jw1003Data.response.toString())

                getAdminInfo(jsonObject)
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
            }

            override fun onError(throwable: Throwable) {
                LogUtil.e("err")
            }

        })

    }

    /**
     * 관리자 정보 가져오기
     * jw1006
     */
    fun getAdminInfo(jsonObject: JsonObject) {
        signInRepository.jw1006(jsonObject, object : SignInRepository.GetResbodyCallback<JSONObject> {
            override fun onSuccess(data: JSONObject) {
                val jw1006Data = gson.fromJson(data.toString(), JW1006::class.java)
                if ("N" == jw1006Data.result) {
                    return
                }
                if ("N" == jw1006Data.resbody?.successYN) {
                    // todo 회원가입필요
                }

                _jw1006Data.value = jw1006Data
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
            }

            override fun onError(throwable: Throwable) {
            }

        })
    }
//
//    /**
//     * sns_id 등록체크
//     * jw1004
//     */
//    fun isSNSIdRegistConfirm(jsonObject: JsonObject) {
//        signInRepository.jw1004(jsonObject, object : SignInRepository.GetResbodyCallback<JW1004> {
//            override fun onSuccess(data: JSONObject) {
//                if ("Y" == data.resbody?.successYN) {
//                    // sns_id가 있는 경우
//                    // todo signIn
//
//                } else if ("N" == data.resbody?.successYN) {
//                    // sns_id가 없는 경우
//                    isEmailOverlapConfirm(jsonObject)
//                }
//
//            }
//
//            override fun onFailure(code: Int) {
//                LogUtil.e(code)
//            }
//
//            override fun onError(throwable: Throwable) {
//            }
//
//        })
//
//    }
//
//
//    /**
//     * 이메일 중복체크
//     * jw1001
//     */
//    fun isEmailOverlapConfirm(jsonObject: JsonObject) {
//        signUpRepository.jw1001(jsonObject, object : SignUpRepository.GetResbodyCallback<JW1001> {
//            override fun onSuccess(data: JW1001) {
//                if ("N" == data.resbody?.emailValid) {
//                    // 이메일이 있는 경우
//                    // todo adminUpdate
//
//                } else if ("Y" == data.resbody?.emailValid) {
//                    // 이메일이 없는 경우
//                    // todo signUp
//
//                }
//            }
//
//            override fun onFailure(code: Int) {
//                LogUtil.e(code)
//            }
//
//            override fun onError(throwable: Throwable) {
//            }
//
//        })
//    }
//
//
//    fun jw1002(jsonObject: JsonObject) {
//        signUpRepository.su1002(jsonObject, object : SignUpRepository.GetResbodyCallback<JW1002> {
//            override fun onSuccess(data: JW1002) {
//                _jw1002Data.value = (data)
//            }
//
//            override fun onFailure(code: Int) {
//                LogUtil.e(code)
//            }
//
//            override fun onError(throwable: Throwable) {
//            }
//
//        })
//    }

}