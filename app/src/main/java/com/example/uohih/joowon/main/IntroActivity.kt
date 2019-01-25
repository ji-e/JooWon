package com.example.uohih.joowon.main

import android.content.Intent
import android.os.Bundle
import com.example.uohih.dailylog.base.JWBaseActivity
import com.example.uohih.joowon.R
import kotlinx.android.synthetic.main.activity_intro.*


/**
 * 인트로
 * 2초 후 MainListActivity()로 이동
 */
class IntroActivity : JWBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        go()
    }




    /**
     * 다음 화면 이동
     */
    fun go(){
        intro_activity.postDelayed({
//            LogUtil.d((getPreference(passwordSetting)))
//            if (getPreference(passwordSetting) != "") {
//                var intent = Intent(this, PasswordCheckActivity::class.java)
//                startActivityForResult(intent, passwordCheck)
//            } else {
//                goPage()
//            }

            var intent = Intent(this, MainListActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
