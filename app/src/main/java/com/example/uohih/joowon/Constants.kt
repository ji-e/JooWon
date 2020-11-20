package com.example.uohih.joowon

object Constants {

    const val SUBSCRIBE_BIRTH = "SUBSCRIBE_BIRTH"

    const val SERVICE_ADMIN = "admin"
    const val SERVICE_EMPLOYEE = "employee"
    const val SERVICE_EMPLOYEE_UPLOAD = "employee/upload"
    const val SERVICE_VACATION = "vacation"
    const val JW1001 = "JW1001"   // 이메일중복확인
    const val JW1002 = "JW1002"   // 회원가입
    const val JW1003 = "JW1003"
    const val JW1004 = "JW1004"
    const val JW1005 = "JW1005"   // 회원정보업데이트
    const val JW1006 = "JW1006"   // 이메일및네이버ID중복확인
    const val JW2001 = "JW2001"   // 로그인
    const val JW2002 = "JW2002"   // 로그아웃
    const val JW2003 = "JW2003"   // 자동로그인
    const val JW3001 = "JW3001"   // 직원리스트조회
    const val JW3002 = "JW3002"   // 직원정보추가
    const val JW3003 = "JW3003"   // 직원정보업데이트
    const val JW3004 = "JW3004"   // 직원삭제하기
    const val JW4001 = "JW4001"   // 휴가등록하기
    const val JW4002 = "JW4002"   // 휴가업데이트
    const val JW4003 = "JW4003"   // 휴가리스트조회
    const val JW4004 = "JW4004"   // 휴가삭제하기


    // PasswordCheckActivity requestCode
    const val passwordCheck = 1000

    // Preference const value
    const val activitySetting = "activitySetting"
    const val passwordSetting = "passwordSetting"
    const val passwordTemp = "passwordTemp"
    const val PREFERENCE_AUTO_SIGNIN_TOKEN = "PREFERENCE_AUTO_SIGNIN_TOKEN"
    const val PREFERENCE_APP_INSTANCE_ID = "PREFERENCE_APP_INSTANCE_ID"
    const val PREFERENCE_FCM_TOKEN = "PREFERENCE_FCM_TOKEN"

    // 정규식
    const val PHONE_NUM_PATTERN = "(\\d{3})(\\d{3,4})(\\d{4})"
    const val YYYYMMDD_PATTERN = "(\\d{4})(\\d{2})(\\d{2})"
    const val EMAIL_PATTERN = "\\w+@\\w+\\.\\w+(\\.\\w+)?"
    const val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$"
    const val PASSWORD_CONTINUE_PATTEN = "(.)\\1\\1\\1"

    // 네이버 api
    const val OAUTH_CLIENT_ID = ""
    const val OAUTH_CLIENT_SECRET = ""
    const val OAUTH_CLIENT_NAME = "네이버 아이디로 로그인"


}
