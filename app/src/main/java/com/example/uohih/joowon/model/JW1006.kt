package com.example.uohih.joowon.model

data class JW1006(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYN: String? = null,      // 관리자정보 존재여부
            val isEmailRegisted: String? = null,// 이메일 등록여부
            val isSnsIdRegisted: String? = null // sns_id 등록여부
    )
}
