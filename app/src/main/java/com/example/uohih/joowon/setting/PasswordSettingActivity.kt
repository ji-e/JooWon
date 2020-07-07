package com.example.uohih.joowon.setting

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.KeyPadAdapter
import com.example.uohih.joowon.base.JWBaseActivity
import kotlinx.android.synthetic.main.activity_password_check.*

class PasswordSettingActivity : JWBaseActivity() {
    private val mAadapter by lazy { KeyPadAdapter(this, setKeyPadData()) }

    private val mIvPwResId = intArrayOf(R.id.iv_pin0, R.id.iv_pin1, R.id.iv_pin2, R.id.iv_pin3, R.id.iv_pin4, R.id.iv_pin5)
    private var mIvPw = arrayOfNulls<ImageView>(mIvPwResId.size)

    private var tempPw = ""
    private var firstPw = ""
    private var resetPw = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_check)

        initView()

    }

    private fun initView() {
        resetPw = getPreference(Constants.passwordSetting)
        if (resetPw.isNotEmpty()) {
            pwcheck_tv.text = getString(R.string.pwsetting_text_confirm)
        } else {
            pwcheck_tv.text = getString(R.string.pwsetting_text01)
        }

        keypad_grid.adapter = mAadapter

        // 키패드 클릭 리스너
        mAadapter.setKeyPadListener(KeyPadListener())

        // pin 입력 리스너
        pwcheck_input.addTextChangedListener(PasswordTextWatcher())

        // pin id 세팅
        for (i in mIvPwResId.indices) {
            val view: ImageView = pwcheck_linear_pin_input.findViewById(mIvPwResId[i])
            mIvPw[i] = view
        }

//        pwcheck_close.setColorFilter(Color.parseColor("#FFFFFF"))
        pwcheck_img_lock.setColorFilter(Color.parseColor("#004680"))

    }

    /**
     * pin 입력 리스너
     */
    private inner class PasswordTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            setPwImage(count)
            if (count == mIvPwResId.size) {
                setPassword()
            }
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    /**
     * 키패드 클릭 리스너
     */
    private inner class KeyPadListener : KeyPadAdapter.KeyPadListener {
        override fun onEraserClickEvent() {
            tempPw = removeLastChar()
            pwcheck_input.text = tempPw
        }

        override fun onNumClickEvent(index: String) {
            tempPw += index
            pwcheck_input.text = tempPw
        }

        override fun onRefreshClickEvent() {
            tempPw = ""
            pwcheck_input.text = tempPw
        }
    }


    fun onClickPw(view: View) {
        when (view.id) {
            R.id.pwcheck_close -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            R.id.pwcheck_linear_pin_input -> {
                pwcheck_input.text = ""
            }
        }
    }

    /**
     * 키패드 클릭시 pin 색 변환
     * inputLen: Int
     */
    private fun setPwImage(inputLen: Int) {
        for (i in mIvPw.indices) {
                mIvPw[i]?.isSelected = i < inputLen
        }
    }

    /**
     * 지움 버튼 클릭시 마지막 글자 지움
     */
    private fun removeLastChar(): String {
        if (tempPw.isNotEmpty()) {
            return tempPw.substring(0, tempPw.length - 1)
        }
        return tempPw
    }

    /**
     * 비밀 번호 6자리 입력시
     */
    fun setPassword() {
        // 비밀번호 재설정
        if (resetPw.isNotEmpty()) {
            if (resetPw == tempPw) {
                pwcheck_tv.text = getString(R.string.pwsetting_text01)
                resetPw = ""
            } else {
                pwcheck_tv.text = getString(R.string.pwcheck_text02)
            }
            pwcheck_input.text = ""
            tempPw = ""
            return
        }

        if (firstPw.isEmpty()) {
            if (tempPw == getPreference(Constants.passwordSetting)) {
                // 기존과 같은 비밀번호를 입력시
                pwcheck_tv.text = getString(R.string.pwsetting_text03)
            } else {
                firstPw = tempPw
                pwcheck_tv.text = getString(R.string.pwsetting_text02)
            }

        } else {
            if (firstPw == tempPw) {
                setPreference(Constants.passwordSetting, firstPw)
                Toast.makeText(this, resources.getString(R.string.pwsetting_text_complete), Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                pwcheck_tv.text = getString(R.string.pwsetting_text_cInconsistency)
                firstPw = ""
            }

        }
        pwcheck_input.text = ""
        tempPw = ""
    }
}
