package com.example.uohih.joowon.data.setting

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

interface SettingDataSource {
    fun jw2002(
            jsonObject: JsonObject,
            success: (JW2002) -> Unit,
            failure: (Throwable) -> Unit
    )
}