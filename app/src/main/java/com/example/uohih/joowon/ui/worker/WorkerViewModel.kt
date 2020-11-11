package com.example.uohih.joowon.ui.worker

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.util.DateCommonUtil
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class WorkerViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : JWBaseViewModel(application, jwBaseRepository) {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _workerInsertForm = MutableLiveData<WorkerInsrtFormState>()
    private val _jw3002Data = MutableLiveData<JW3002>()
    private val _jw3003Data = MutableLiveData<JW3003>()
    private val _jw3004Data = MutableLiveData<JW3004>()
    private val _jw4003Data = MutableLiveData<JW4003>()

    val liveEmployeeInfo = MutableLiveData<JW3001ResBodyList>()
    val liveVacationList = MutableLiveData<ArrayList<VacationList>>()
    val liveViewPagerInfo = MutableLiveData<ArrayList<String>>()
    val liveCalendarInfo = MutableLiveData<LocalDate>()
    var liveCalendarList = ArrayList<CalendarDayInfo>()


    val isLoading: LiveData<Boolean> = _isLoading
    val workerInsertForm: LiveData<WorkerInsrtFormState> = _workerInsertForm
    val jw3002Data: LiveData<JW3002> = _jw3002Data
    val jw3003Data: LiveData<JW3003> = _jw3003Data
    val jw3004Data: LiveData<JW3004> = _jw3004Data
    val jw4003Data: LiveData<JW4003> = _jw4003Data

    val today = LocalDate.now().toString()

    fun setEmployeeInfo(_id: String) {
        liveEmployeeInfo.value = UICommonUtil.getEmployeeInfo(_id)

    }

    fun setInitWorkerMainActivity() {
        liveViewPagerInfo.postValue(arrayListOf("grid", "recycle"))
    }

    fun setCalendarList(date: LocalDate) {
        liveCalendarList = (JWBaseActivity().getCalendar(date))
        liveCalendarInfo.value = date
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
        jwBaseRepository.requestBaseUploadService(part, jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                _isLoading.value = false
                val jw3002Data = Gson().fromJson(data.toString(), JW3002::class.java)
                _jw3002Data.value = jw3002Data

            }

            override fun onFailure(code: Int) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }
        })
    }

    /**
     * 직원 업데이트
     * jw3003
     */
    fun updateEmployee(part: MultipartBody.Part, jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestBaseUploadService(part, jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                _isLoading.value = false
                val jw3003Data = Gson().fromJson(data.toString(), JW3003::class.java)

                if ("Y" == jw3003Data.resbody?.successYn) {
                    val bundle = Bundle()
//                    bundle.putString("profile_image",jsonObject.get("profile_image").toString())
                    bundle.putString("name", jsonObject.get("name").asString)
                    bundle.putString("phone_number", (Constants.PHONE_NUM_PATTERN).toRegex().replace(jsonObject.get("phone_number").asString, "$1-$2-$3"))
                    bundle.putString("entered_date", (Constants.YYYYMMDD_PATTERN).toRegex().replace(jsonObject.get("entered_date").asString, "$1-$2-$3"))
                    bundle.putString("birth", (Constants.YYYYMMDD_PATTERN).toRegex().replace(jsonObject.get("birth").asString, "$1-$2-$3"))
                    bundle.putString("total_vacation_cnt", jsonObject.get("total_vacation_cnt").asString)
                    UICommonUtil.setEmployeeInfo(jsonObject.get("_id").asString, bundle, null)

                }
                _jw3003Data.value = jw3003Data

            }

            override fun onFailure(code: Int) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }
        })
    }


    /**
     * 직원삭제하기
     * jw3004
     */
    fun deleteEmployee(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestBaseService(jsonObject, Constants.SERVICE_EMPLOYEE, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                val jw3004Data = Gson().fromJson(data.toString(), JW3004::class.java)

                _jw3004Data.value = jw3004Data
                _isLoading.value = false
            }

            override fun onFailure(code: Int) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }
        })
    }


    /**
     * 휴가리스트가져오기
     * JW4003
     */
    fun findVacation(localDate: LocalDate, jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestBaseService(jsonObject, Constants.SERVICE_VACATION, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {

                val jw4003Data = Gson().fromJson(data.toString(), JW4003::class.java)
                _jw4003Data.value = jw4003Data

                liveVacationList.value = (jw4003Data.resbody?.vacationList)
                setCalendarList(localDate)

                _isLoading.value = false
            }

            override fun onFailure(code: Int) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
                _isNetworkErr.value = true
            }
        })
    }
}