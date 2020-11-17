package com.example.uohih.joowon.data.signup

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface SignUpRepository {
    /**
     * 회원가입
     */
    fun signUp(
            jsonObject: JsonObject,
            success: (JW1002) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 로그인
     */
    fun signIn(
            jsonObject: JsonObject,
            success: (JW2001) -> Unit,
            failure: (Throwable) -> Unit
    )
}