package com.example.uohih.joowon.ui.main

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.JW3001
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

class MainListViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : AndroidViewModel(application) {
    private val _isLoading = MutableLiveData<Boolean>()
    private var _jw3001Data = MutableLiveData<JW3001>()

    val isLoading: LiveData<Boolean> = _isLoading
    val jw3001Data: LiveData<JW3001> = _jw3001Data

    val liveEmployeeList = MutableLiveData<List<JW3001ResBodyList>>()

    /**
     * 직원리스트 가져오기
     * jw3001
     */
    fun getEmployeeList(jsonObject: JsonObject) {
        _isLoading.value = true
        jwBaseRepository.requestBaseService(jsonObject, object : GetResbodyCallback {
            override fun onSuccess(code: Int, data: JSONObject) {
                _isLoading.value = false

                val jw3001Data = Gson().fromJson(data.toString(), JW3001::class.java)

                if ("false" == jw3001Data.result) {
                    return
                }
                if ("N" == jw3001Data.resbody?.successYn) {
                    return
                }
                val employeeList = mutableListOf<JW3001ResBodyList>()

                for (i in 0 until (jw3001Data.resbody?.employeeList?.size ?: 0)) {
                    val item = jw3001Data.resbody?.employeeList?.get(i)

                    val _id = item?._id
                    val profileImage = item?.profile_image
                    val name = item?.name
                    val phoneNumber = (Constants.PHONE_NUM_PATTERN).toRegex().replace(item?.phone_number.toString(), "$1-$2-$3")
                    val birth = item?.birth
                    val enteredDate = (Constants.YYYYMMDD_PATTERN).toRegex().replace((item?.entered_date)?.substring(0, 8).toString(), "$1-$2-$3")
                    val totalVacationCnt = item?.total_vacation_cnt?.toInt() ?: 0
                    val remainVacationCnt = item?.remain_vacation_cnt?.toInt() ?: 0
                    val useVacationCnt = (totalVacationCnt - remainVacationCnt)

                    employeeList.add(JW3001ResBodyList(_id, profileImage, name, phoneNumber, birth, enteredDate, totalVacationCnt.toString(), remainVacationCnt.toString(), useVacationCnt.toString()))
                }

                liveEmployeeList.postValue(employeeList)

                _jw3001Data.value = jw3001Data
            }

            override fun onFailure(code: Int) {
                _isLoading.value = false
            }

            override fun onError(throwable: Throwable) {
                _isLoading.value = false
            }
        })
    }

}