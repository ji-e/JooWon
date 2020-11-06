package com.example.uohih.joowon.ui.worker

import androidx.lifecycle.AndroidViewModel
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
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import java.time.LocalDate

class WorkerViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : JWBaseViewModel(application, jwBaseRepository) {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _workerInsertForm = MutableLiveData<WorkerInsrtFormState>()
    private val _jw3002Data = MutableLiveData<JW3002>()
    private val _jw4003Data = MutableLiveData<JW4003>()

    val liveEmployeeInfo = MutableLiveData<JW3001ResBodyList>()
    val liveVacationList = MutableLiveData<ArrayList<VacationList>>()
    val liveViewPagerInfo = MutableLiveData<ArrayList<String>>()
    var liveCalendarList = ArrayList<CalendarDayInfo>()

    val isLoading: LiveData<Boolean> = _isLoading
    val workerInsertForm: LiveData<WorkerInsrtFormState> = _workerInsertForm
    val jw3002Data: LiveData<JW3002> = _jw3002Data
    val jw4003Data: LiveData<JW4003> = _jw4003Data

    val today = DateCommonUtil().setFormatHpDate(DateCommonUtil().getToday().get("yyyymmdd").toString())


    fun setEmployeeInfo(_id: String, date: LocalDate) {
        liveEmployeeInfo.value = UICommonUtil.getEmployeeInfo(_id)
        liveViewPagerInfo.postValue(arrayListOf("grid", "recycle"))
        liveVacationList.postValue(liveEmployeeInfo.value?.use_vacation)
        liveCalendarList = (JWBaseActivity().getCalendar(date))
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

    fun findVacationList(jsonObject: JsonObject) {

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
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
            }
        })
    }


    /**
     * 휴가리스트가져오기
     * JW4003
     */
    fun findVacation(jsonObject: JsonObject) {
        jwBaseRepository.requestBaseService(jsonObject, Constants.SERVICE_VACATION, object : GetResbodyCallback {

            override fun onSuccess(code: Int, data: JSONObject) {

                val jw4001Data = Gson().fromJson(data.toString(), JW4003::class.java)
                _jw4003Data.value = jw4001Data
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