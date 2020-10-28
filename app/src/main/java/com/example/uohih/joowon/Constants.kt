package com.example.uohih.joowon

class Constants {
    companion object {

        val SERVICE_ADMIN = "admin"
        val SERVICE_EMPLOYEE = "employee"
        val SERVICE_EMPLOYEE_UPLOAD = "employee/upload"
        val JW1001 = "JW1001"   // 이메일 중복확인
        val JW1002 = "JW1002"   // 회원가입
        val JW1003 = "JW1003"
        val JW1004 = "JW1004"
        val JW1005 = "JW1005"   // 회원정보 업데이트
        val JW1006 = "JW1006"   // 이메일 및 네이버ID 중복확인
        val JW2001 = "JW2001"   // 로그인
        val JW2002 = "JW2002"   // 로그아웃
        val JW2003 = "JW2003"   // 자동로그인
        val JW3001 = "JW3001"   // 직원리스트 가져오기
        val JW3002 = "JW3002"   // 직원정보 추가하기


        // PasswordCheckActivity requestCode
        val passwordCheck = 1000

        // Preference value
        val activitySetting = "activitySetting"
        val passwordSetting = "passwordSetting"
        val passwordTemp = "passwordTemp"
        val PREFERENCE_AUTO_SIGNIN_TOKEN = "PREFERENCE_AUTO_SIGNIN_TOKEN"
        val PREFERENCE_APP_INSTANCE_ID = "PREFERENCE_APP_INSTANCE_ID"

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
