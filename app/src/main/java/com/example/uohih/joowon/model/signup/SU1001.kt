package com.example.uohih.joowon.model.signup

import com.example.uohih.joowon.model.BaseResBody

/**
 * 아이디 중복확인
 */
data class SU1001(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val emailValid: String? = null, // 이메일 검증 여부
            val email: String? = null       // 이메일
    )
}
