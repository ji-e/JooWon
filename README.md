# JooWon
직원 및 근태 관리   


### 1. 목적
부모님께서 직원 및 근태관리를 수기로 작성하는 모습을 보고 공장 직원 관리를 위한 안드로이드 애플리케이션 개발   
- Kotlin 연습
- MVVM + DataBinding + LiveData구현 연습


### 2. 개발환경
- Language: Kotlin
- Server: Heroku:Node.js
- DB: MongoDB:Mongoose
- Tools: AndroidStudio, GitHub


### 3. 기능
##### 1) 로그인
- 로컬 회원가입
- 로컬 로그인
- 네이버 아이디로 로그인: 네이버 Oauth 2.0연동
##### 2) 메인
- 직원리스트
  - RecyclerView로 구현
  - swipe-layout라이브러리 이용으로 밀어서 직원삭제 구현
- 직원등록버튼
  - FAB
##### 3) 직원등록
- 프로필 사진: 갤러리/카메라
- 생년월일/입사날짜: DatePicker로 구현
##### 4) 휴가등록
- 직원 검색 및 검색결과 리스트: RecyclerView로 구현
- 휴가등록
  - 커스텀 캘린더 다이얼로그 구현: 
    - ViewPager2를 이용하여 상하로 스와이프 가능
    - GridViewdml setOnTouchListener()를 이용하여 드래그로 날짜 범위 선택 가능
    
    
##### 5) 직원관리
- 직원 정보 및 휴가 정보
-------구현중-------------

  

