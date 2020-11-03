package com.example.uohih.joowon.model

data class JW4001(
        val resbody: resbodyInfo? = null

) : BaseResBody() {
    data class resbodyInfo(
            val successYn: String? = null)  //휴가등록성공여부
}