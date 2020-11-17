package com.example.uohih.joowon.data.main

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface MainListRepository {
    /**
     * 직원리스트 가져오기
     */
    fun getEmployeeList(
            jsonObject: JsonObject,
            success: (JW3001) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 직원삭제하기
     */
    fun deleteEmployee(
            jsonObject: JsonObject,
            success: (JW3004) -> Unit,
            failure: (Throwable) -> Unit
    )
}