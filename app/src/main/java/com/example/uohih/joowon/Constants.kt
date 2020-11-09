package com.example.uohih.joowon

class Constants {
    companion object {

        val SERVICE_ADMIN = "admin"
        val SERVICE_EMPLOYEE = "employee"
        val SERVICE_EMPLOYEE_UPLOAD = "employee/upload"
        val SERVICE_VACATION = "vacation"
        val JW1001 = "JW1001"   // 이메일중복확인
        val JW1002 = "JW1002"   // 회원가입
        val JW1003 = "JW1003"
        val JW1004 = "JW1004"
        val JW1005 = "JW1005"   // 회원정보

        // 업데이트
        val JW1006 = "JW1006"   // 이메일및네이버ID중복확인
        val JW2001 = "JW2001"   // 로그인
        val JW2002 = "JW2002"   // 로그아웃
        val JW2003 = "JW2003"   // 자동로그인
        val JW3001 = "JW3001"   // 직원리스트조회
        val JW3002 = "JW3002"   // 직원정보추가
        val JW3003 = "JW3003"   // 직원정보업데이트
        val JW3004 = "JW3004"   // 직원삭제하기
        val JW4001 = "JW4001"   // 휴가등록하기
        val JW4002 = "JW4002"   // 휴가업데이트
        val JW4003 = "JW4003"   // 휴가리스트조회
        val JW4004 = "JW4004"   // 휴가삭제하기


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
        val OAUTH_CLIENT_ID = ""
        val OAUTH_CLIENT_SECRET = ""
        val OAUTH_CLIENT_NAME = "네이버 아이디로 로그인"
    }


}
