package com.example.uohih.joowon.model

data class JW3004(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYn: String? = null    // 직원삭제성공여부
    )
}
