package com.example.uohih.joowon.model.signup

import com.example.uohih.joowon.model.BaseResBody

data class SU1002(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYN: String? = null   // 회원가입 검증여부
    )
}
