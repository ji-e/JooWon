package com.example.uohih.joowon.model

data class JW1004(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYN: String? = null   // 회원가입 검증여부
    )
}
