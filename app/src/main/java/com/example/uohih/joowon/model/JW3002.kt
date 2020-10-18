package com.example.uohih.joowon.model

data class JW3002(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val addEmployeeValid: String? = null   // 직원추가 성공여부
    )
}