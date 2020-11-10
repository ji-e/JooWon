package com.example.uohih.joowon.model

data class VacationList(
        val vacation_date: String? = null,      // 휴가날짜
        val vacation_content: String? = null,   // 휴가내용
        val vacation_cnt: String? = "1.0",      // 휴가사용개수
        val vacation_id: String? = null,
        val _id: String?
)