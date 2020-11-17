package com.example.uohih.joowon.data.signin

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

class SignInRepositoryImpl(
        private val signInDataSource: SignInDataSource
) : SignInRepository {

    override fun isEmailOverlapConfirm(jsonObject: JsonObject, success: (JW1001) -> Unit, failure: (Throwable) -> Unit) {
        signInDataSource.jw1001(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun signUp(jsonObject: JsonObject, success: (JW1002) -> Unit, failure: (Throwable) -> Unit) {
        signInDataSource.jw1002(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun getSnsSignInInfo(accessToken: String, success: (JW1003) -> Unit, failure: (Throwable) -> Unit) {
        signInDataSource.jw1003(
                accessToken = accessToken,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun updateAdminInfo(jsonObject: JsonObject, success: (JW1005) -> Unit, failure: (Throwable) -> Unit) {
        signInDataSource.jw1005(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun getAdminInfo(jsonObject: JsonObject, success: (JW1006) -> Unit, failure: (Throwable) -> Unit) {
        signInDataSource.jw1006(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun signIn(jsonObject: JsonObject, success: (JW2001) -> Unit, failure: (Throwable) -> Unit) {
        signInDataSource.jw2001(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }
}