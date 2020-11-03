package com.example.uohih.joowon.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.uohih.joowon.R

//import com.example.uohih.joowon.ui.vacation.VacationFragment

class BlankActivity : JWBaseActivity() {

    private var fragmentFlag = ""
    private var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)

        if (intent.hasExtra("fragmentFlag")) {
            fragmentFlag = intent.getStringExtra("fragmentFlag")

        }

        if (intent.hasExtra("fragmentBundle")) {
            bundle = intent.getBundleExtra("fragmentBundle")
        }

        initView()

    }

    private fun initView() {
        var fragment: Fragment? = null

        if ("vacation" == fragmentFlag) {
//            fragment = VacationFragment.newInstance(bundle)
        }


        if (fragment != null)
            replaceContainerFragment(R.id.blank_frame, fragment)

    }
}

