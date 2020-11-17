package com.example.uohih.joowon.data.signin

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface SignInRepository {
    /**
     * 아이디확인
     */
    fun isEmailOverlapConfirm(
            jsonObject: JsonObject,
            success: (JW1001) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 회원가입
     */
    fun signUp(
            jsonObject: JsonObject,
            success: (JW1002) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 네이버로그인 정보 가져오기
     */
    fun getSnsSignInInfo(
            accessToken: String,
            success: (JW1003) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 회원정보업데이트
     */
    fun updateAdminInfo(
            jsonObject: JsonObject,
            success: (JW1005) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 관리자 정보 가져오기
     */
    fun getAdminInfo(
            jsonObject: JsonObject,
            success: (JW1006) -> Unit,
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