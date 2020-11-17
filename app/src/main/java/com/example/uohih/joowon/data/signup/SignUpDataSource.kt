package com.example.uohih.joowon.data.signup

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface SignUpDataSource {
    fun jw1002(
            jsonObject: JsonObject,
            success: (JW1002) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw2001(
            jsonObject: JsonObject,
            success: (JW2001) -> Unit,
            failure: (Throwable) -> Unit
    )
}