package com.example.uohih.joowon.data.worker

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody

interface WorkerDataSource {
    fun jw3002(
            part: MultipartBody.Part,
            jsonObject: JsonObject,
            success: (JW3002) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw3003(
            part: MultipartBody.Part,
            jsonObject: JsonObject,
            success: (JW3003) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw3004(
            jsonObject: JsonObject,
            success: (JW3004) -> Unit,
            failure: (Throwable) -> Unit
    )

    fun jw4003(
            jsonObject: JsonObject,
            success: (JW4003) -> Unit,
            failure: (Throwable) -> Unit
    )
}