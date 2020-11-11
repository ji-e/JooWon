package com.example.uohih.joowon.model

data class JW4004(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYn: String? = null    // 휴가삭제성공여부
    )
}