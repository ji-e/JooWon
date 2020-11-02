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
import com.google.gson.JsonObject
import org.json.JSONObject
import java.text.FieldPosition
import java.util.regex.Pattern

class VacationViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : JWBaseViewModel(application, jwBaseRepository) {

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading

    var initEmployeeList = UICommonUtil.getInitEmployeeList()
    var searchEmployeeList = mutableListOf<JW3001ResBodyList>()
    val liveEmployeeList = MutableLiveData<List<JW3001ResBodyList>>()

    val liveVacationList = MutableLiveData<List<VacationList>>()
    val liveEmployeeInfo = MutableLiveData<JW3001ResBodyList>()

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


    fun setEmployeeInfo(position: Int) {
        LogUtil.e(searchEmployeeList)
        val info = searchEmployeeList[position]

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
    }
}