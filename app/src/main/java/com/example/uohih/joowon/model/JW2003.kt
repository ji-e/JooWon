package com.example.uohih.joowon.model

data class JW2003(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val signInValid: String? = null,    // 로그인 성공여부
            val autoToken: String? = null       // 자동로그인 토큰
    )
}
