package com.example.uohih.joowon.ui.signin

import com.example.uohih.joowon.data.signin.SignInRepository
import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

class MockSignInRepository : SignInRepository {

    override fun isEmailOverlapConfirm(jsonObject: JsonObject, success: (JW1001) -> Unit, failure: (Throwable) -> Unit) {

    }

    override fun signUp(jsonObject: JsonObject, success: (JW1002) -> Unit, failure: (Throwable) -> Unit) {

    }

    override fun getSnsSignInInfo(accessToken: String, success: (JW1003) -> Unit, failure: (Throwable) -> Unit) {

    }

    override fun updateAdminInfo(jsonObject: JsonObject, success: (JW1005) -> Unit, failure: (Throwable) -> Unit) {

    }

    override fun getAdminInfo(jsonObject: JsonObject, success: (JW1006) -> Unit, failure: (Throwable) -> Unit) {

    }

    override fun signIn(jsonObject: JsonObject, success: (JW2001) -> Unit, failure: (Throwable) -> Unit) {

    }

}