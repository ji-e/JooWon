package com.example.uohih.joowon.setting

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.KeyPadAdapter
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import kotlinx.android.synthetic.main.activity_password_check.*

class PasswordSettingActivity : JWBaseActivity() {
    private val mAadapter by lazy { KeyPadAdapter(this, setkeyPadData()) }

    private val mIvPwResId = intArrayOf(R.id.iv_pin0, R.id.iv_pin1, R.id.iv_pin2, R.id.iv_pin3, R.id.iv_pin4, R.id.iv_pin5)
    private var mIvPw = arrayOfNulls<ImageView>(mIvPwResId.size)

    private var str: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_check)

        initView()

    }

    private fun initView() {
        keypad_grid.adapter = mAadapter
        /**
         * 키패드 클릭 리스너
         */
        mAadapter.setKeyPadListener(object : KeyPadAdapter.KeyPadListener {
            override fun onEraserClickEvent() {
                str = removeLastChar()
                pwcheck_input.text = str
            }

            override fun onNumClickEvent(index: String) {
                str += index
                pwcheck_input.text = str
            }
        })

        // 핀 클릭 리스너
        pwcheck_linear_pin_input.setOnClickListener {
            pwcheck_input.text = ""
        }

        // pin id 세팅
        for (i in mIvPwResId.indices) {
            val view: ImageView = pwcheck_linear_pin_input.findViewById(mIvPwResId[i])
            mIvPw[i] = view
            LogUtil.d(mIvPw[i].toString())
        }


        pwcheck_input.addTextChangedListener(PasswordTextWatcher())

        pwcheck_close.setColorFilter(Color.parseColor("#FFFFFF"))
        pwcheck_img_lock.setColorFilter(Color.parseColor("#004680"))

    }

    /**
     * 비밀번호 입력 할 때 마다 리스너
     */
    inner class PasswordTextWatcher : TextWatcher {
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


    fun btnOnClick(view: View) {
        when (view.id) {
            R.id.pwcheck_close -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    /**
     * 키패드 클릭시 pin 색 변환
     * inputLen: Int
     */
    private fun setPwImage(inputLen: Int) {
        for (i in mIvPw.indices) {
            if (mIvPw[i] != null) {
                mIvPw[i]?.isSelected = i < inputLen
            }
        }
    }

    /**
     * 지움 버튼 클릭시 마지막 글자 지움
     */
    private fun removeLastChar(): String {
        if (str.isNotEmpty()) {
            return str.substring(0, str.length - 1)
        }
        return str
    }

    /**
     * 비밀 번호 6자리 입력시
     */
    fun setPassword() {
        LogUtil.d(str)
        val pw = getPreference(Constants.passwordSetting)
        if (pw == str) {
//            if (reset) {
//                setPreference(Constants.passwordSetting, "")
//                Toast.makeText(this, resources.getString(R.string.reset_msg), Toast.LENGTH_SHORT).show()
//            } else
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            pwcheck_tv.text = getString(R.string.pwcheck_text02)
            pwcheck_input.text = ""
            str = ""
        }
    }


}
