package com.example.uohih.joowon.model

/**
 * 아이디 중복확인
 */
data class JW1001(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val emailValid: String? = null, // 이메일 검증 여부 (N: 있음, Y: 없음)
            val email: String? = null       // 이메일
    )
}
