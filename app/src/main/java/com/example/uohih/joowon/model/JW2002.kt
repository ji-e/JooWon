package com.example.uohih.joowon.model

data class JW2002(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val signOutValid: String? = null   // 로그아웃 여부
    )
}
