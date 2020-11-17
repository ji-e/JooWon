package com.example.uohih.joowon.data.worker

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody

interface WorkerRepository {
    /**
     * 직원등록
     */
    fun addEmployee(
            part: MultipartBody.Part,
            jsonObject: JsonObject,
            success: (JW3002) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 직원업데이트
     */
    fun updateEmployee(
            part: MultipartBody.Part,
            jsonObject: JsonObject,
            success: (JW3003) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 직원삭제
     */
    fun deleteEmployee(
            jsonObject: JsonObject,
            success: (JW3004) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 휴가리스트가져오기
     */
    fun findVacation(
            jsonObject: JsonObject,
            success: (JW4003) -> Unit,
            failure: (Throwable) -> Unit
    )
}