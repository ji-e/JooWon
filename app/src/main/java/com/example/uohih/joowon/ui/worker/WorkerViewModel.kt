package com.example.uohih.joowon.ui.worker

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.model.JW3001
import com.example.uohih.joowon.model.JW3001ResBodyList
import com.example.uohih.joowon.model.SignUpFormState
import com.example.uohih.joowon.model.WorkerInsrtFormState
import com.example.uohih.joowon.repository.JWBaseRepository
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.example.uohih.joowon.util.DateCommonUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import java.util.regex.Pattern

class WorkerViewModel(application: JWBaseApplication, private val jwBaseRepository: JWBaseRepository) : AndroidViewModel(application) {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _workerInsertForm = MutableLiveData<WorkerInsrtFormState>()

    val isLoading: LiveData<Boolean> = _isLoading
    val workerInsertForm: LiveData<WorkerInsrtFormState> = _workerInsertForm
    val today = DateCommonUtil().setFormatDate(DateCommonUtil().getToday().get("yyyymmdd").toString())

    fun isDataValidCheck() {
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