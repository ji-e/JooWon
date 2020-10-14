package com.example.uohih.joowon.model

import androidx.lifecycle.MutableLiveData

data class JW3001(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYn: String? = null,    // 직원리스트조회성공여부
            val employeeList: List<JW3001ResBodyList>? = null

    )
}