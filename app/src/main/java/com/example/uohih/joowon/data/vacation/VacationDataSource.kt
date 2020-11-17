package com.example.uohih.joowon.data.vacation

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface VacationDataSource {
    fun jw4001(
            jsonObject: JsonObject,
            success: (JW4001) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw4004(
            jsonObject: JsonObject,
            success: (JW4004) -> Unit,
            failure: (Throwable) -> Unit
    )
}