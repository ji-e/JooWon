package com.example.uohih.joowon.data.main

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface MainListDataSource {
    fun jw3001(
            jsonObject: JsonObject,
            success: (JW3001) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw3004(
            jsonObject: JsonObject,
            success: (JW3004) -> Unit,
            failure: (Throwable) -> Unit
    )
}