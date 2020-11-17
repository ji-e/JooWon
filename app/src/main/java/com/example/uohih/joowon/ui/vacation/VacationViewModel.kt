package com.example.uohih.joowon.ui.vacation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.data.vacation.VacationRepository
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.model.JW4001
import com.example.uohih.joowon.model.JW4004
import com.example.uohih.joowon.model.VacationList
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class VacationViewModel(private val vacationRepository: VacationRepository) : JWBaseViewModel() {

    private val _isBtnRegisterState = MutableLiveData<Boolean>()
    private val _jw4001Data = MutableLiveData<JW4001>()
    private val _jw4004Data = MutableLiveData<JW4004>()
    private val _liveVacationList = MutableLiveData<List<VacationList>>()
    private val _liveVacationInfo = MutableLiveData<VacationList>()
    private val _liveEmployeeList = MutableLiveData<List<JW3001ResBodyList>>()
    private val _liveEmployeeInfo = MutableLiveData<JW3001ResBodyList>()

    val isBtnRegisterState: LiveData<Boolean> = _isBtnRegisterState
    val jw4001Data: LiveData<JW4001> = _jw4001Data  // 휴가등록
    val jw4004Data: LiveData<JW4004> = _jw4004Data  // 휴가삭제
    val liveVacationList: LiveData<List<VacationList>> = _liveVacationList
    val liveVacationInfo: LiveData<VacationList> = _liveVacationInfo
    val liveEmployeeList: LiveData<List<JW3001ResBodyList>> = _liveEmployeeList
    val liveEmployeeInfo: LiveData<JW3001ResBodyList> = _liveEmployeeInfo

    var initEmployeeList = UICommonUtil.getInitEmployeeList()
    var searchEmployeeList = mutableListOf<JW3001ResBodyList>()
    var vacationList = mutableListOf<VacationList>()




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
        _liveEmployeeList.postValue(employeeList)
        searchEmployeeList = employeeList
    }

    fun setEmployeeList() {
        _liveEmployeeList.postValue(initEmployeeList)
        searchEmployeeList = initEmployeeList
    }

    fun setEmployeeInfo(_id: String) {
        _liveEmployeeInfo.value = UICommonUtil.getEmployeeInfo(_id)
    }

    fun setVacationInfo(extras: Bundle) {
        _liveVacationInfo.value = VacationList(
                extras.getString("vacation_date"),
                extras.getString("vacation_content"),
                extras.getString("vacation_cnt"),
                extras.getString("_id"),
                extras.getString("vacation_id")
        )
    }

    fun setInitVacationList() {
        _liveVacationList.postValue(vacationList)
        _isBtnRegisterState.value = false
    }

    fun addVacationList(vacationList: MutableList<VacationList>) {
        this.vacationList = vacationList
        _liveVacationList.postValue(vacationList)
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

        vacationRepository.registerVacation(
                jsonObject = jsonObject,
                success = {
                    _jw4001Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e( "" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )

    }

    /**
     * 휴가삭제
     * JW4004
     */
    fun deleteVacation(jsonObject: JsonObject) {
        _isLoading.value = true
        jsonObject.addProperty("vacation_id", liveVacationInfo.value?._id)

        vacationRepository.deleteVacation(
                jsonObject = jsonObject,
                success = {
                    _jw4004Data.value = it
                    _isLoading.value = false
                },
                failure = {
                    LogUtil.e( "" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }
}