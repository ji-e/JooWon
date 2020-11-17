package com.example.uohih.joowon.data.setting

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

class SettingRepositoryImpl(
        private val settingDataSource: SettingDataSource
) : SettingRepository {

    override fun signOut(jsonObject: JsonObject, success: (JW2002) -> Unit, failure: (Throwable) -> Unit) {
        settingDataSource.jw2002(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }
}