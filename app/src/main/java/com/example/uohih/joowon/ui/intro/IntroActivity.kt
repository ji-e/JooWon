package com.example.uohih.joowon.ui.intro

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.database.AsyncCallback
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.databinding.ActivityIntroBinding
import com.example.uohih.joowon.repository.GoogleIdAsyncTask
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.ui.signin.SignInActivity
import com.example.uohih.joowon.ui.signin.SignInViewModel
import com.example.uohih.joowon.ui.signin.SignInViewModelFactory
import com.example.uohih.joowon.util.UICommonUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_intro.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * 인트로
 * 2초 후 MainListActivity()로 이동
 */
class IntroActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }

    private lateinit var mListener: PermissionListener
    private lateinit var signInViewModel: SignInViewModel

    private val customDialog by lazy {
        CustomDialog(mContext, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityIntroBinding>(this, R.layout.activity_intro)

        signInViewModel = ViewModelProviders.of(this, SignInViewModelFactory()).get(SignInViewModel::class.java)

        binding.signInVm = signInViewModel
        binding.lifecycleOwner = this

        setObserve()

        //step1
        permissionCheck()

    }

    /**
     * step1
     * permissionCheck
     * 퍼미션 체크 (저장공간, 카메라)
     */
    private fun permissionCheck() {
        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE)
        val arrayList = ArrayList<String>()

        for (i in permission.indices) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission[i])

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                arrayList.add(permission[i])
            }
        }

        val arry = arrayOfNulls<String>(arrayList.size)
        for (i in 0 until arrayList.size) {
            arry[i] = arrayList[i]
        }

        if (arrayList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, arry, 0)

            setPermissionListener(object : PermissionListener {
                override fun onFail() {
                    finish()
                }

                override fun onSuccess() {
                    // step2
                    nextActivity()
                }
            })
        }

        if (arrayList.isEmpty()) {
            // step2
            nextActivity()
        }
    }


    /**
     * step2
     * 다음 액티비티로 이동
     */
    fun nextActivity() {

        // 앱 인스턴스 ID 설정
//        setInstanceID()

        val androidId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        val v = GoogleIdAsyncTask(this).apply {
            setGoogleAsyncCallback(object : AsyncCallback {
                override fun onPostExecute(adid: Any?) {
                    adid?.let {
                        val appInstanceID = UUID(androidId.hashCode().toLong(), adid.hashCode().toLong()).toString()
                        if (appInstanceID != UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID)) {
                            UICommonUtil.setPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID, appInstanceID)
                        }
                    }

                    val instanceId = UICommonUtil.getPreferencesData(Constants.PREFERENCE_APP_INSTANCE_ID)
                    val autoSigninToken = UICommonUtil.getPreferencesData(Constants.PREFERENCE_AUTO_SIGNIN_TOKEN)

                    if (autoSigninToken.isNotEmpty()) {
                        // 자동로그인
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("methodid", Constants.JW2003)
                        jsonObject.addProperty("provider", instanceId)
                        jsonObject.addProperty("autoToken", autoSigninToken)
                        jsonObject.addProperty("email", "email")
                        jsonObject.addProperty("password", "password")
                        signInViewModel.signIn(jsonObject)
                    } else {
                        // 로그인 화면으로 이동
                        intro_activity.postDelayed({
                            //            LogUtil.d((getPreference(Constants.passwordSetting)))
//            //설정된 비밀번호가 존재할 경우
//            if (getPreference(Constants.passwordSetting).isNotEmpty()) {
//                val intent = Intent(this, PasswordCheckActivity::class.java)
//                startActivityForResult(intent, Constants.passwordCheck)
//            }
//            //설정된 비밀번호가 존재하지 않을 경우
//            else{
////                goMainActivity()
//
////                val intent = Intent(this, LoginActivity::class.java)
                            val intent = Intent(thisActivity, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
//            }
                        }, 2000)
                    }
                }

                override fun doInBackground() {
                }

            })
        }
        v.executeSync()


    }


    private fun goMainActivity() {
        val dbHelper = DBHelper(this)
//        LogUtil.d(dbHelper.getTableExiste(dbHelper.tableNameVacationJW).count)
//        if (dbHelper.getTableExiste(dbHelper.tableNameVacationJW).count <= 0)
        dbHelper.createVacationTable()

        val intent = Intent(this, MainListActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.passwordCheck -> {
                    goMainActivity()
                }
            }
        } else {
            exit()
        }
    }

    /** ----------- permission check start ----------*/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    mListener.onFail()
                } else {
                    mListener.onSuccess()
                }
            }
        }
    }

    interface PermissionListener {
        fun onFail()
        fun onSuccess()
    }

    private fun setPermissionListener(listener: PermissionListener) {
        mListener = listener
    }

    /** ----------- permission check end ----------*/


    private fun setObserve() {
        signInViewModel.jw2001Data.observe(this@IntroActivity, Observer {
            val jw2001Data = it ?: return@Observer

            if ("Y" == jw2001Data.resbody?.signInValid) {
                // todo 로그인완료?
                jw2001Data.resbody.autoToken?.let { it1 -> UICommonUtil.setPreferencesData(Constants.PREFERENCE_AUTO_SIGNIN_TOKEN, it1) }
                goMainActivity()
            } else {
                customDialog.showDialog(
                        thisActivity,
                        getString(R.string.signin_err2),
                        getString(R.string.btnConfirm), DialogInterface.OnClickListener { dialog, which ->
                    UICommonUtil.setPreferencesData(Constants.PREFERENCE_AUTO_SIGNIN_TOKEN, "")
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                })
            }
        })
    }
}
