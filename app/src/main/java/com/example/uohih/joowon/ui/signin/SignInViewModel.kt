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
                val jw1003Data = Gson().fromJson(data.toString(), JW1003::class.java)

                val jsonObject = JsonObject()
                jsonObject.addProperty("methodid", Constants.JW1006)
                jsonObject.addProperty("email", jw1003Data.response?.email)
                jsonObject.addProperty("sns_id", jw1003Data.response?.id)


                val snsProvider = JsonObject()
                snsProvider.addProperty("id", jw1003Data.response?.id)
                snsProvider.addProperty("email", jw1003Data.response?.email)
                snsProvider.addProperty("name", jw1003Data.response?.name)

                jsonObject.add("sns_provider", snsProvider)
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

                val jw1006Data = Gson().fromJson(data.toString(), JW1006::class.java)
                LogUtil.d(jw1006Data.resbody?.successYn.toString(), jw1006Data.result.toString())
                if ("false" == jw1006Data.result) {
                    return
                }
                if ("N" == jw1006Data.resbody?.successYn) {
                    jsonObject.addProperty("methodid", Constants.JW1002)
                    signIn(jsonObject)
                    return
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


    /**
     * 회원가입
     * jw1002
     */
    fun signIn(jsonObject: JsonObject) {
        signUpRepository.jw1002(jsonObject, object : SignUpRepository.GetResbodyCallback<JSONObject> {
            override fun onSuccess(data: JSONObject) {
                val jw1002Data = Gson().fromJson(data.toString(), JW1002::class.java)
                if ("N" == jw1002Data.result) {
                    return
                }
                _jw1002Data.value = jw1002Data
            }

            override fun onFailure(code: Int) {
                LogUtil.e(code)
            }

            override fun onError(throwable: Throwable) {
            }

        })
    }

}