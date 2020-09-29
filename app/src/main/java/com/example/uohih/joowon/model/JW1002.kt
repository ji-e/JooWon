package com.example.uohih.joowon.model

data class JW1002(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYn: String? = null   // 회원가입 검증여부
    )
}
