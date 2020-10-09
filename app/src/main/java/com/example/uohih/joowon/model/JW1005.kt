package com.example.uohih.joowon.model

data class JW1005(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val adminUpdateValid: String? = null   // 회원가입 검증여부
    )
}
