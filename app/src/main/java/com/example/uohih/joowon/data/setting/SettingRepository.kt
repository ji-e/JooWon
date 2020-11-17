package com.example.uohih.joowon.data.setting

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface SettingRepository {
    /**
     * 로그아웃
     */
    fun signOut(
            jsonObject: JsonObject,
            success: (JW2002) -> Unit,
            failure: (Throwable) -> Unit
    )
}