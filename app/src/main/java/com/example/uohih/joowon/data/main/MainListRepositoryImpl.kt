package com.example.uohih.joowon.data.main

import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject

class MainListRepositoryImpl(
        private val mainListDataSource: MainListDataSource
) : MainListRepository {

    override fun getEmployeeList(jsonObject: JsonObject, success: (JW3001) -> Unit, failure: (Throwable) -> Unit) {
        mainListDataSource.jw3001(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun deleteEmployee(jsonObject: JsonObject, success: (JW3004) -> Unit, failure: (Throwable) -> Unit) {
        mainListDataSource.jw3004(
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