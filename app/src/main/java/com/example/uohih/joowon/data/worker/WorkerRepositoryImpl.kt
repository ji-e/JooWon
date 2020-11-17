package com.example.uohih.joowon.data.worker

import com.example.uohih.joowon.data.vacation.VacationRepository
import com.example.uohih.joowon.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody

class WorkerRepositoryImpl(
        private val workerDataSource: WorkerDataSource
) : WorkerRepository {

    override fun addEmployee(part: MultipartBody.Part, jsonObject: JsonObject, success: (JW3002) -> Unit, failure: (Throwable) -> Unit) {
        workerDataSource.jw3002(
                part = part,
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun updateEmployee(part: MultipartBody.Part, jsonObject: JsonObject, success: (JW3003) -> Unit, failure: (Throwable) -> Unit) {
        workerDataSource.jw3003(
                part = part,
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
        workerDataSource.jw3004(
                jsonObject = jsonObject,
                success = {
                    success(it)
                },
                failure = {
                    failure(Throwable())
                }
        )
    }

    override fun findVacation(jsonObject: JsonObject, success: (JW4003) -> Unit, failure: (Throwable) -> Unit) {
        workerDataSource.jw4003(
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