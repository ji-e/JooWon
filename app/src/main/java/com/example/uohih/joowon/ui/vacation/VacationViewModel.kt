package com.example.uohih.joowon.ui.vacation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONObject
import java.time.LocalDate

class VacationViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : JWBaseViewModel(application, jwBaseRepository) {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isBtnRegisterState = MutableLiveData<Boolean>()
    private val _jw4001Data = MutableLiveData<JW4001>()
    private val _jw4004Data = MutableLiveData<JW4004>()
    private val _vacationInfo = MutableLiveData<VacationList>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isBtnRegisterState: LiveData<Boolean> = _isBtnRegisterState
    val jw4001Data: LiveData<JW4001> = _jw4001Data  // 휴가등록
    val jw4004Data: LiveData<JW4004> = _jw4004Data  // 휴가삭제

    var initEmployeeList = UICommonUtil.getInitEmployeeList()
    var searchEmployeeList = mutableListOf<JW3001ResBodyList>()
    val liveEmployeeList = MutableLiveData<List<JW3001ResBodyList>>()

    val liveVacationList = MutableLiveData<List<VacationList>>()
    val liveEmployeeInfo = MutableLiveData<JW3001ResBodyList>()
    var vacationList = mutableListOf<VacationList>()
    val vacationInfo: LiveData<VacationList> = _vacationInfo


    fun setEmployeeList() {
        liveEmployeeList.postValue(initEmployeeList)
        searchEmployeeList = initEmployeeList
    }

    /**
     * 검색결과리스트가져오기
     */
    fun getSearchResultList(edtText: String) {
        val employeeList = mutableListOf<JW3001ResBodyList>()

        for (i in 0 until initEmployeeList.size) {
            val list = initEmployeeList[i]
            if (list.name.toString().contains(edtText) || list.phone_number.toString().contains(edtText)) {
                employeeList.add(list)
            }
        }
        liveEmployeeList.postValue(employeeList)
        searchEmployeeList = employeeList
    }


    fun setEmployeeInfo(_id: String) {
        liveEmployeeInfo.value = UICommonUtil.getEmployeeInfo(_id)
    }

    fun setVacationInfo(extras: Bundle) {
        _vacationInfo.value = VacationList(
                extras.getString("vacation_date"),
                extras.getString("vacation_content"),
                extras.getString("vacation_cnt"),
                extras.getString("_id"),
                extras.getString("vacation_id")
        )
    }

    fun setInitVacationList() {
        liveVacationList.postValue(vacationList)
        _isBtnRegisterState.value = false
    }

    fun addVacationList(vacationList: MutableList<VacationList>) {
        this.vacationList = vacationList
        liveVacationList.postValue(vacationList)
        _isBtnRegisterState.value = vacationList.size > 0
    }


    /**
     * 휴가등록
     * JW4001
     */
    fun registerVacation(jsonObject: JsonObject) {
        _isLoading.value = true
        val paramInsertData = JsonArray()
        val vacationExistList = UICommonUtil.getVacationList(liveEmployeeInfo.value?._id)
                ?: arrayListOf()
        for (i in 0 until vacationList.size) {

            // 이미 등록되 휴가가 있을 경우
            val isVacationExist = UICommonUtil.getVacationInfo(vacationList[i].vacation_date.toString(), vacationExistList)
            if (isVacationExist != null) {
                _jw4001Data.value = JW4001(resbody = null)
                _isLoading.value = false
                return
            }


            // 휴가 등록
            val vObject = JsonObject()
            vObject.addProperty("vacation_date", vacationList[i].vacation_date)
            vObject.addProperty("vacation_content", vacationList[i].vacation_content)
            vObject.addProperty("vacation_cnt", vacationList[i].vacation_cnt)
            vObject.addProperty("vacation_id", liveEmployeeInfo.value?._id)
            paramInsertData.add(vObject)
        }
        jsonObject.addProperty("vacation_id", liveEmployeeInfo.value?._id)
        jsonObject.add("paramInsertData", paramInsertData)

        jwBaseRepository.requestBaseService(jsonObject, Constants.SERVICE_VACATION, object : GetResbodyCallback {

            override fun onSuccess(code: Int, data: JSONObject) {

                val jw4001Data = Gson().fromJson(data.toString(), JW4001::class.java)
                _jw4001Data.value = jw4001Data
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
     * 휴가삭제
     * JW4004
     */
    fun deleteVacation(jsonObject: JsonObject) {
        _isLoading.value = true
        jsonObject.addProperty("vacation_id", vacationInfo.value?._id)

        jwBaseRepository.requestBaseService(jsonObject, Constants.SERVICE_VACATION, object : GetResbodyCallback {

            override fun onSuccess(code: Int, data: JSONObject) {
                val jw4004Data = Gson().fromJson(data.toString(), JW4004::class.java)
                _jw4004Data.value = jw4004Data
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