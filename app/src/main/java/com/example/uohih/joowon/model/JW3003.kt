package com.example.uohih.joowon.model

data class JW3003(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYn: String? = null   // 직원업데이트 성공여부
    )
}