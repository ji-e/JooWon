package com.example.uohih.joowon.ui.worker

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.data.worker.WorkerRepository
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import java.time.LocalDate

class WorkerViewModel(private val workerRepository: WorkerRepository) : JWBaseViewModel() {

    private val _workerInsertForm = MutableLiveData<WorkerInsrtFormState>()
    private val _jw3002Data = MutableLiveData<JW3002>()
    private val _jw3003Data = MutableLiveData<JW3003>()
    private val _jw3004Data = MutableLiveData<JW3004>()
    private val _jw4003Data = MutableLiveData<JW4003>()
    private val _liveEmployeeInfo = MutableLiveData<JW3001ResBodyList>()
    private val _liveVacationList = MutableLiveData<ArrayList<VacationList>>()
    private val _liveViewPagerInfo = MutableLiveData<ArrayList<String>>()
    private val _liveCalendarInfo = MutableLiveData<LocalDate>()

    val workerInsertForm: LiveData<WorkerInsrtFormState> = _workerInsertForm
    val jw3002Data: LiveData<JW3002> = _jw3002Data
    val jw3003Data: LiveData<JW3003> = _jw3003Data
    val jw3004Data: LiveData<JW3004> = _jw3004Data
    val jw4003Data: LiveData<JW4003> = _jw4003Data
    val liveEmployeeInfo: LiveData<JW3001ResBodyList> = _liveEmployeeInfo
    val liveVacationList: LiveData<ArrayList<VacationList>> = _liveVacationList
    val liveViewPagerInfo: LiveData<ArrayList<String>> = _liveViewPagerInfo
    val liveCalendarInfo: LiveData<LocalDate> = _liveCalendarInfo

    var liveCalendarList = ArrayList<CalendarDayInfo>()
    val today = LocalDate.now().toString()

    fun setEmployeeInfo(_id: String) {
        _liveEmployeeInfo.value = UICommonUtil.getEmployeeInfo(_id)
    }

    fun setInitWorkerMainActivity() {
        _liveViewPagerInfo.postValue(arrayListOf("grid", "recycle"))
    }

    fun setCalendarList(date: LocalDate) {
        liveCalendarList = (JWBaseActivity().getCalendar(date))
        _liveCalendarInfo.value = date
    }

    private fun isDataValidCheck() {
        if (_workerInsertForm.value?.nameMsg == null) {
            _workerInsertForm.value = WorkerInsrtFormState(isDataValid = true)
        }
    }

    fun signUpDataChanged(name: String) {
        var nameError: Int? = null

        if (name.isEmpty()) {
            nameError = R.string.workerInsert_name2
        }
        _workerInsertForm.value = WorkerInsrtFormState(
                nameMsg = nameError)

        isDataValidCheck()
    }

    /**
     * 직원 추가하기
     * jw3002
     */
    fun addEmployee(part: MultipartBody.Part, jsonObject: JsonObject) {
        _isLoading.value = true

        workerRepository.addEmployee(
                part = part,
                jsonObject = jsonObject,
                success = {
                    _jw3002Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )

    }

    /**
     * 직원 업데이트
     * jw3003
     */
    fun updateEmployee(part: MultipartBody.Part, jsonObject: JsonObject) {
        _isLoading.value = true

        workerRepository.updateEmployee(
                part = part,
                jsonObject = jsonObject,
                success = {
                    if ("Y" == it.resbody?.successYn) {
                        val bundle = Bundle()
//                    bundle.putString("profile_image",jsonObject.get("profile_image").toString())
                        bundle.putString("name", jsonObject.get("name").asString)
                        bundle.putString("phone_number", (Constants.PHONE_NUM_PATTERN).toRegex().replace(jsonObject.get("phone_number").asString, "$1-$2-$3"))
                        bundle.putString("entered_date", (Constants.YYYYMMDD_PATTERN).toRegex().replace(jsonObject.get("entered_date").asString, "$1-$2-$3"))
                        bundle.putString("birth", (Constants.YYYYMMDD_PATTERN).toRegex().replace(jsonObject.get("birth").asString, "$1-$2-$3"))
                        bundle.putString("total_vacation_cnt", jsonObject.get("total_vacation_cnt").asString)
                        UICommonUtil.setEmployeeInfo(jsonObject.get("_id").asString, bundle, null)

                    }

                    _jw3003Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }


    /**
     * 직원삭제하기
     * jw3004
     */
    fun deleteEmployee(jsonObject: JsonObject) {
        _isLoading.value = true

        workerRepository.deleteEmployee(
                jsonObject = jsonObject,
                success = {
                    _jw3004Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }


    /**
     * 휴가리스트가져오기
     * JW4003
     */
    fun findVacation(localDate: LocalDate, jsonObject: JsonObject) {
        _isLoading.value = true

        workerRepository.findVacation(
                jsonObject = jsonObject,
                success = {
                    _jw4003Data.value = it
                    _liveVacationList.value = (it.resbody?.vacationList)
                    setCalendarList(localDate)
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }
}