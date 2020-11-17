package com.example.uohih.joowon.data.vacation

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface VacationRepository {
    /**
     * 휴가등록하기
     */
    fun registerVacation(
            jsonObject: JsonObject,
            success: (JW4001) -> Unit,
            failure: (Throwable) -> Unit
    )

    /**
     * 휴가삭제하기
     */
    fun deleteVacation(
            jsonObject: JsonObject,
            success: (JW4004) -> Unit,
            failure: (Throwable) -> Unit
    )
}