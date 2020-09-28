package com.example.uohih.joowon

class Constants {
    companion object {

        val SERVICE_ADMIN = "admin"
        val JW1001 = "SU1001"
        val JW1002 = "SU1002"
        val JW1003 = "SU1003"
        val JW1004 = "SU1004"
        val JW1005 = "SU1005"
        val JW1006 = "SU1006"


        // PasswordCheckActivity requestCode
        val passwordCheck = 1000

        // Preference value
        val activitySetting = "activitySetting"
        val passwordSetting = "passwordSetting"
        val passwordTemp = "passwordTemp"


        // 정규식
        val PHONE_NUM_PATTERN = "(\\d{3})(\\d{3,4})(\\d{4})"
        val YYYYMMDD_PATTERN = "(\\d{4})(\\d{2})(\\d{2})"
        val EMAIL_PATTERN = "\\w+@\\w+\\.\\w+(\\.\\w+)?"
        val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$"
        val PASSWORD_CONTINUE_PATTEN = "(.)\\1\\1\\1"

        // 네이버 api
        val OAUTH_CLIENT_ID = "SSVjX00IPK8w6O_rjzaw"
        val OAUTH_CLIENT_SECRET = "TbfvxZ6tBf"
        val OAUTH_CLIENT_NAME = "네이버 아이디로 로그인"
    }


}