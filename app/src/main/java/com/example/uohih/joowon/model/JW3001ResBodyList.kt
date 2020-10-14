package com.example.uohih.joowon.model

data class JW3001ResBodyList(
        val _id: String? = null,
        val profile_image: String? = null,       // 프로필사진
        val name: String? = null,                // 이름
        val phone_number: String? = null,        // 핸드폰번호
        val birth: String? = null,               // 생년월일
        val entered_date: String? = null,        // 입사일
        val total_vacation_cnt: String? = null,  // 총휴가일수
        val remain_vacation_cnt: String? = null, // 남은휴가일수
        val use_vacation_cnt: String? = null
)