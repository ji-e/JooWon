package com.example.uohih.joowon.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.model.JW3001
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.model.JW3004
import com.example.uohih.joowon.data.main.MainListRepository
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject

class MainListViewModel(private val mainListRepository: MainListRepository) : JWBaseViewModel() {

    private var _jw3001Data = MutableLiveData<JW3001>()
    private var _jw3004Data = MutableLiveData<JW3004>()
    private val _searchData = MutableLiveData<Boolean>()
    private var _liveEmployeeList = MutableLiveData<List<JW3001ResBodyList>>()

    val jw3001Data: LiveData<JW3001> = _jw3001Data
    val jw3004Data: LiveData<JW3004> = _jw3004Data
    val searchData: LiveData<Boolean> = _searchData
    val liveEmployeeList: LiveData<List<JW3001ResBodyList>> = _liveEmployeeList

    var initEmployeeList = mutableListOf<JW3001ResBodyList>()
    var searchEmployeeList = mutableListOf<JW3001ResBodyList>()


    fun setLiveEmployeeList(searchEmployeeList: MutableList<JW3001ResBodyList>) {
        _liveEmployeeList.postValue(searchEmployeeList)
    }
    /**
     * 직원리스트 가져오기
     * jw3001
     */
    fun getEmployeeList(jsonObject: JsonObject) {
        _isLoading.value = true

        mainListRepository.getEmployeeList(
                jsonObject = jsonObject,
                success = {
                    _isLoading.value = false
                    if ("false" == it.result) {
                        return@getEmployeeList
                    }
                    if ("N" == it.resbody?.successYn) {
                        return@getEmployeeList
                    }
                    val employeeList = mutableListOf<JW3001ResBodyList>()

                    for (i in 0 until (it.resbody?.employeeList?.size ?: 0)) {
                        val item = it.resbody?.employeeList?.get(i)

                        val _id = item?._id
                        val profileImage = item?.profile_image
                        val name = item?.name
                        val phoneNumber = (Constants.PHONE_NUM_PATTERN).toRegex().replace(item?.phone_number.toString(), "$1-$2-$3")
                        val birth = (Constants.YYYYMMDD_PATTERN).toRegex().replace((item?.birth)?.substring(0, 8).toString(), "$1-$2-$3")
                        val enteredDate = (Constants.YYYYMMDD_PATTERN).toRegex().replace((item?.entered_date)?.substring(0, 8).toString(), "$1-$2-$3")
                        val totalVacationCnt = item?.total_vacation_cnt?.toInt() ?: 0
                        val useVacation = item?.use_vacation
                        val useVacationSize = useVacation?.size ?: 0
                        var useVacationCnt = 0f

                        for (i in 0 until useVacationSize) {
                            useVacationCnt += (useVacation?.get(i)?.vacation_cnt)?.toFloat() ?: 0f
                        }

                        employeeList.add(JW3001ResBodyList(_id, profileImage, name, phoneNumber, birth, enteredDate, totalVacationCnt.toString(), useVacation, useVacationCnt.toString()))
                    }

                    _liveEmployeeList.postValue(employeeList)
                    initEmployeeList = employeeList
                    searchEmployeeList = employeeList

                    UICommonUtil.setInitEmployeeList(initEmployeeList)
                    _jw3001Data.value = it
                },
                failure = {
                    LogUtil.e("" + it)
                    _isLoading.value = false
                    _isNetworkErr.value = true
                }
        )
    }


    /**
     * 검색결과리스트가져오기
     */
    fun getSearchResultList(edtText: String) {
        val _employeeList = mutableListOf<JW3001ResBodyList>()

        for (i in 0 until initEmployeeList.size) {
            val list = initEmployeeList[i]
            if (list.name.toString().contains(edtText) || list.phone_number.toString().contains(edtText)) {
                _employeeList.add(list)
            }
        }
        _liveEmployeeList.postValue(_employeeList)
        searchEmployeeList = _employeeList
        _searchData.value = true
    }

    /**
     * 직원삭제하기
     * jw3004
     */
    fun deleteEmployee(jsonObject: JsonObject, position: Int) {

        mainListRepository.deleteEmployee(
                jsonObject = jsonObject,
                success = {
                    if ("false" == it.result) {
                        return@deleteEmployee
                    }
                    if ("N" == it.resbody?.successYn) {
                        return@deleteEmployee
                    }

                    searchEmployeeList.removeAt(position)
                    _liveEmployeeList.postValue(searchEmployeeList)
                    UICommonUtil.setInitEmployeeList(searchEmployeeList)
                    _jw3004Data.value = it

                },
                failure = {
                    LogUtil.e("" + it)
                    _isNetworkErr.value = true
                }
        )
    }
}