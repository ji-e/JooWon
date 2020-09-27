package com.example.uohih.joowon.model.signup

import com.example.uohih.joowon.model.BaseResBody

/**
 * 아이디 중복확인
 */
data class SU1003(
        val resultcode: String,
        val message: String,
        val resbody: resbodyInfo? = null
) {
    data class resbodyInfo(
            val id: String? = null,     // 고유식별id
            val email: String? = null,  // 이메일
            val name: String? = null    // 이름
    )
}
