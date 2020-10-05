package com.example.uohih.joowon.model

data class JW2001(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val signInValid: String? = null
    )
}
