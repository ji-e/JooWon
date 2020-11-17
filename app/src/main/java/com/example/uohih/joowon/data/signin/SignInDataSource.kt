package com.example.uohih.joowon.data.signin

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface SignInDataSource {
    fun jw1001(
            jsonObject: JsonObject,
            success: (JW1001) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw1002(
            jsonObject: JsonObject,
            success: (JW1002) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw1003(
            accessToken: String,
            success: (JW1003) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw1005(
            jsonObject: JsonObject,
            success: (JW1005) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw1006(
            jsonObject: JsonObject,
            success: (JW1006) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw2001(
            jsonObject: JsonObject,
            success: (JW2001) -> Unit,
            failure: (Throwable) -> Unit
    )
}