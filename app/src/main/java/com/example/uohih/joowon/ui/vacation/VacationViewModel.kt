package com.example.uohih.joowon.ui.vacation

import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.JWBaseViewModel
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.model.*
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONObject
import java.text.FieldPosition
import java.util.regex.Pattern

class VacationViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : JWBaseViewModel(application, jwBaseRepository) {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isBtnRegisterState = MutableLiveData<Boolean>()
    private val _jw4001Data = MutableLiveData<JW4001>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isBtnRegisterState: LiveData<Boolean> = _isBtnRegisterState
    val jw4001Data: LiveData<JW4001> = _jw4001Data  // 휴가등록

    var initEmployeeList = UICommonUtil.getInitEmployeeList()
    var searchEmployeeList = mutableListOf<JW3001ResBodyList>()
    val liveEmployeeList = MutableLiveData<List<JW3001ResBodyList>>()

    val liveVacationList = MutableLiveData<List<VacationList>>()
    val liveEmployeeInfo = MutableLiveData<JW3001ResBodyList>()
    var vacationList = mutableListOf<VacationList>()



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
        for (i in 0 until initEmployeeList.size) {
            val info = initEmployeeList[i]
            if (info._id == _id) {
                liveEmployeeInfo.value = JW3001ResBodyList(
                        _id = info._id,
                        profile_image = info.profile_image,
                        name = info.name,
                        phone_number = info.phone_number,
                        birth = info.birth,
                        entered_date = info.entered_date,
                        total_vacation_cnt = info.total_vacation_cnt,
                        use_vacation = info.use_vacation,
                        use_vacation_cnt = info.use_vacation_cnt

                )
                return
            }
        }
    }

    fun setInitVacationList() {
//        val vacationList = mutableListOf<VacationList>()
//        vacationList.add(VacationList("휴가일자", "내용", "근태일수"))
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
        for (i in 0 until vacationList.size) {
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
}