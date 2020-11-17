package com.example.uohih.joowon.data.vacation

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

class VacationRepositoryImpl(
        private val vacationDataSource: VacationDataSource
) : VacationRepository {

    override fun registerVacation(jsonObject: JsonObject, success: (JW4001) -> Unit, failure: (Throwable) -> Unit) {
        vacationDataSource.jw4001(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun deleteVacation(jsonObject: JsonObject, success: (JW4004) -> Unit, failure: (Throwable) -> Unit) {
        vacationDataSource.jw4004(
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