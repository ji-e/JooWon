package com.example.uohih.joowon.model

data class JW0000(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val signInValid: String? = null   // 로그인세션여부
    )
}
