package com.example.uohih.joowon.model

/**
 * 아이디 중복확인
 */
data class JW1003(
        val resultcode: String,
        val message: String,
        val response: resbodyInfo? = null
) {
    data class resbodyInfo(
            val id: String? = null,     // 고유식별id
            val email: String? = null,  // 이메일
            val name: String? = null    // 이름
    )
}
