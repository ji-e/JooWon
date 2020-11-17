package com.example.uohih.joowon.data.signup

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

class SignUpRepositoryImpl(
        private val signUpDataSource: SignUpDataSource
) : SignUpRepository {

    override fun signUp(jsonObject: JsonObject, success: (JW1002) -> Unit, failure: (Throwable) -> Unit) {
        signUpDataSource.jw1002(
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
        signUpDataSource.jw2001(
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