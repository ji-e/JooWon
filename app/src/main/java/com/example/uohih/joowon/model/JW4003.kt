package com.example.uohih.joowon.model

data class JW4003(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYn: String? = null,    // 직원리스트조회성공여부
            val vacationList: ArrayList<VacationList>? = null)
}