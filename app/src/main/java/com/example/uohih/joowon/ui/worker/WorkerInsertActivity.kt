package com.example.uohih.joowon.ui.worker

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.databinding.ActivityWorkerInsertBinding
import com.example.uohih.joowon.ui.adapter.DialogListAdapter
import com.example.uohih.joowon.ui.customView.*
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.util.DateCommonUtil
import com.example.uohih.joowon.util.LogUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.btn_positive_bottom.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

/**
 * 직원추가
 */
class WorkerInsertActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }
    private val workerViewModel: WorkerViewModel by viewModel()
    private lateinit var binding: ActivityWorkerInsertBinding

    private var imageFilePath: String = ""
    private var _id = ""

    private lateinit var tvTitleView: TopTitleView
    private lateinit var layDelete: LinearLayout
    private lateinit var layProfile: ConstraintLayout
    private lateinit var imgProfile: ImageView
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtTotalCnt: EditText
    private lateinit var tvBirthDate: TextView
    private lateinit var tvEnjoyDate: TextView
    private lateinit var btnNameDelete: ImageButton
    private lateinit var btnPhoneDelete: ImageButton
    private lateinit var btnResister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(thisActivity, R.layout.activity_worker_insert)
        binding.run {
            lifecycleOwner = thisActivity
            workerInsertVm = workerViewModel
        }

        val args = intent
        if (args.hasExtra("_id")) {
            _id = args.getStringExtra("_id").toString()
            workerViewModel.setEmployeeInfo(_id)
        }


        initView()

    }

    private fun initView() {
        tvTitleView = binding.workerInsertTitleView
        layDelete = binding.workerInsertLayDelete
        layProfile = binding.workerInsertLayProfileImg
        imgProfile = binding.workerInsertImgProfileImg
        edtName = binding.workerInsertEdtName
        edtPhone = binding.workerInsertEdtPhone
        edtTotalCnt = binding.workerInsertEdtTotalCnt
        tvBirthDate = binding.workerInsertTvBirthDate
        tvEnjoyDate = binding.workerInsertTvEnjoyDate
        btnNameDelete = binding.workerInsertBtnNameDelete
        btnPhoneDelete = binding.workerInsertBtnPhoneDelete
        btnResister = binding.workerInsertBtnRegister.btnPositive

        tvEnjoyDate.isFocusableInTouchMode = true

        edtName.addTextChangedListener(WorkerInsertTextWatcher(edtName))
        edtPhone.addTextChangedListener(WorkerInsertTextWatcher(edtPhone))
        edtPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        edtName.onFocusChangeListener = WorkerInsertFocusChangeListener()
        edtPhone.onFocusChangeListener = WorkerInsertFocusChangeListener()
        tvBirthDate.onFocusChangeListener = WorkerInsertFocusChangeListener()
        tvEnjoyDate.onFocusChangeListener = WorkerInsertFocusChangeListener()

        edtTotalCnt.setOnEditorActionListener(WorkerInsertEditActionListener())


        if (_id.isNotEmpty()) {
            tvTitleView.setTitle(getString(R.string.workerUpdate_title))
            layDelete.visibility = View.VISIBLE
            btnResister.text = getString(R.string.workerUpdate_update)
            btnResister.setOnClickListener {
                if (binding.workerInsertBtnRegister.isEnabled) {
                    // 업데이트하기
                    updateEmployee()
                }
            }
        } else {
            btnResister.text = getString(R.string.workerInsert_btn_register)
            btnResister.setOnClickListener {
                if (binding.workerInsertBtnRegister.isEnabled) {
                    // 등록하기
                    addEmployee()
                }
            }
        }

        setObserve()
    }

    private fun setObserve() {
        workerViewModel.isLoading.observe(thisActivity, Observer {
            val isLoading = it ?: return@Observer

            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })

        workerViewModel.jw3002Data.observe(thisActivity, Observer {
            val jw3002Data = it ?: return@Observer
            val customDialog = CustomDialog(mContext)
            if ("failure" == jw3002Data.result) {
                customDialog.apply {
                    setBottomDialog(
                            jw3002Data.msg.toString(),
                            getString(R.string.btnConfirm), null)
                }
                customDialog.show()
                return@Observer
            }
            if ("Y" == jw3002Data.resbody?.successYn) {
                customDialog.apply {
                    setBottomDialog(
                            getString(R.string.workerInsert_dialog_msg),
                            getString(R.string.btnConfirm),
                            View.OnClickListener {
                                dismiss()
                                thisActivity.finish()
                            })

                }
                customDialog.show()
            }
        })

        workerViewModel.jw3003Data.observe(thisActivity, Observer {
            val jw3003Data = it ?: return@Observer
            val customDialog = CustomDialog(mContext)
            if ("failure" == jw3003Data.result) {
                customDialog.apply {
                    setBottomDialog(
                            jw3003Data.msg.toString(),
                            getString(R.string.btnConfirm), null)
                }
                customDialog.show()
                return@Observer
            }
            if ("Y" == jw3003Data.resbody?.successYn) {
                customDialog.apply {
                    setBottomDialog(
                            getString(R.string.workerUpdate_dialog_msg),
                            getString(R.string.btnConfirm),
                            View.OnClickListener {
                                dismiss()
                                finish()
                            })
                }
                customDialog.show()
            }
        })

        workerViewModel.jw3004Data.observe(thisActivity, Observer {
            val jw3004Data = it ?: return@Observer
            val customDialog = CustomDialog(mContext)
            if ("failure" == jw3004Data.result) {
                customDialog.apply {
                    setBottomDialog(
                            jw3004Data.msg.toString(),
                            getString(R.string.btnConfirm), null)
                }
                customDialog.show()
                return@Observer
            }

            if ("Y" == jw3004Data.resbody?.successYn) {
                val result = Intent().apply {
                    putExtras(bundleOf("WORKER_DELETE" to "Y")
                    )
                }

                setResult(Activity.RESULT_OK, result)
                finish()
//               val intent = Intent(thisActivity, MainListActivity::class.java)
//
//                startActivity(intent)
            }
        })
    }


    /**
     * 텍스트 입력 리스너
     */
    private inner class WorkerInsertTextWatcher(private val mEditText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            when (mEditText) {
                edtName -> {
                    //이름
                    btnNameDelete.visibility =
                            if (charSequence.isNotEmpty()) View.VISIBLE
                            else View.GONE
                }
                edtPhone -> {
                    //핸드폰번호
                    btnPhoneDelete.visibility =
                            if (charSequence.isNotEmpty()) View.VISIBLE
                            else View.GONE
                }
            }

            workerViewModel.signUpDataChanged(
                    edtName.text.toString()
            )
        }

        override fun afterTextChanged(s: Editable) {}
    }

    /**
     * 키패드 액션리스너
     */
    private inner class WorkerInsertEditActionListener : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                signUpViewModel.signUpFormState.value?.isDataValid.let { if (it == true) signUp() }
                return false
            }
            return true

        }
    }

    /**
     * 포커스 체인지 리스너
     */
    private inner class WorkerInsertFocusChangeListener : View.OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hasFocus) {
                when (v) {
                    edtName -> btnNameDelete.visibility =
                            if (edtName.text.isNotEmpty()) View.VISIBLE
                            else View.GONE
                    edtPhone -> btnPhoneDelete.visibility =
                            if (edtPhone.text.isNotEmpty()) View.VISIBLE
                            else View.GONE
                    tvBirthDate -> showCalendarDialog(tvBirthDate, false)
                    tvEnjoyDate -> showCalendarDialog(tvEnjoyDate, true)
                }
            } else {
                when (v) {
                    edtName -> btnNameDelete.visibility = View.GONE
                    edtPhone -> btnPhoneDelete.visibility = View.GONE
                }
            }
        }
    }

    fun onClickWorkerInsert(v: View?) {
        when (v) {
            btnNameDelete -> {
                edtName.setText("")
            }
            btnPhoneDelete -> {
                edtPhone.setText("")
            }
            tvBirthDate, binding.workerInsertBtnBirthCalendar -> {
                showCalendarDialog(tvBirthDate, false)
            }
            tvEnjoyDate, binding.workerInsertBtnEnjoyCalendar -> {
                showCalendarDialog(tvEnjoyDate, true)
            }
            layProfile -> {
                showProfileDialog()
            }
            layDelete -> {
                showDeleteDialog()
            }
        }
    }


    /**
     * 직원 추가하기
     */
    private fun addEmployee() {
        val photo = File(imageFilePath)
        val photoBody = RequestBody.create(MediaType.parse("image/jpg"), photo)
        var body = MultipartBody.Part.createFormData("photo", photo.name, photoBody)

        if (imageFilePath.isEmpty()) {
            body = MultipartBody.Part.createFormData("photo", "")
        }

        val jsonObject = JsonObject()
        jsonObject.addProperty("methodid", Constants.JW3002)
        jsonObject.addProperty("name", edtName.text.toString())
        jsonObject.addProperty("phone_number", edtPhone.text.toString().replace("-", ""))
        jsonObject.addProperty("birth", tvBirthDate.text.toString().replace("-", ""))
        jsonObject.addProperty("entered_date", tvEnjoyDate.text.toString().replace("-", ""))
        jsonObject.addProperty("total_vacation_cnt", edtTotalCnt.text.toString())
        jsonObject.addProperty("remain_vacation_cnt", edtTotalCnt.text.toString())

        workerViewModel.addEmployee(body, jsonObject)
    }

    /**
     * 직원 업데이트
     */
    private fun updateEmployee() {
        val photo = File(imageFilePath)
        val photoBody = RequestBody.create(MediaType.parse("image/jpg"), photo)
        var body = MultipartBody.Part.createFormData("photo", photo.name, photoBody)

        if (imageFilePath.isEmpty()) {
            body = MultipartBody.Part.createFormData("photo", "")
        }

        val jsonObject = JsonObject()

        jsonObject.addProperty("methodid", Constants.JW3003)
        jsonObject.addProperty("_id", _id)
        jsonObject.addProperty("name", edtName.text.toString())
        jsonObject.addProperty("phone_number", edtPhone.text.toString().replace("-", ""))
        jsonObject.addProperty("birth", tvBirthDate.text.toString().replace("-", ""))
        jsonObject.addProperty("entered_date", tvEnjoyDate.text.toString().replace("-", ""))
        jsonObject.addProperty("total_vacation_cnt", edtTotalCnt.text.toString())
        jsonObject.addProperty("remain_vacation_cnt", edtTotalCnt.text.toString())

        workerViewModel.updateEmployee(body, jsonObject)
    }

    /**
     * 직원 삭제
     */
    private fun deleteEmployee() {
        val jsonObject = JsonObject()

        jsonObject.addProperty("methodid", Constants.JW3004)
        jsonObject.addProperty("_id", _id)
        LogUtil.e(jsonObject.get("_id"))
        workerViewModel.deleteEmployee(jsonObject)
    }

    /**
     * 직원삭제 다이얼로그
     */
    private fun showDeleteDialog() {
        val customDialog = CustomDialog(mContext).apply {
            setBottomDialog(
                    getString(R.string.dialog_title),
                    String.format(getString(R.string.workerUpdate_delete_msg), edtName.text.toString()),
                    null,
                    getString(R.string.btnCancel), null,
                    getString(R.string.btnConfirm),
                    View.OnClickListener {
                        deleteEmployee()
                        dismiss()
                    })
        }
        customDialog.show()
    }


    /**
     * 프로필 다이얼로그
     */
    private fun showProfileDialog() {
        val customWhDialog = CustomWhDialog(mContext).apply {
            setBottomDialog(
                    getString(R.string.workerInsert_picture),
                    "",
                    View.OnClickListener { dismiss() },
                    getString(R.string.menu01),
                    View.OnClickListener {
                        sendTakePhotoIntent()
                        dismiss()
                    },
                    getString(R.string.menu02),
                    View.OnClickListener {
                        sendTakeGalleryIntent()
                        dismiss()
                    }
            )
        }

        customWhDialog.show()

    }

    /**
     * 캘린더 다이얼로그
     */
    private fun showCalendarDialog(textView: TextView, isFutureSelect: Boolean) {
        val calendarDialog = CalendarDialog(thisActivity).apply {
            setBottomDialog(
                    textView.text.toString(),
                    null,
                    object : CalendarDialog.ConfirmBtnClickListener {
                        override fun onConfirmClick(date: ArrayList<LocalDate>) {
                            LogUtil.e(date)
                            textView.text = date[0].toString()
                        }
                    },
                    isFutureSelect = isFutureSelect,
                    isSelectedMulti = false,
                    isSelectedRang = false,
                    isVisibleCalendar = false)
        }
        calendarDialog.show()
    }


    /**
     * 사진 이미지 생성
     * return File
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val imageFileName = timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir    /* directory */
        )
        imageFilePath = image.absolutePath

        return image
    }


    private var photoUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1000
    private val REQUEST_IMAGE_GALLERY = 2000

    /**
     * 카메라
     */
    private fun sendTakePhotoIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {

            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, packageName, photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    /**
     * 갤러리
     */
    private fun sendTakeGalleryIntent() {
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {

        }
        photoUri = photoFile?.let { FileProvider.getUriForFile(this, packageName, it) }
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val bitmap = BitmapFactory.decodeFile(imageFilePath)
                    lateinit var exif: ExifInterface

                    try {
                        exif = ExifInterface(imageFilePath)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val exifOrientation: Int
                    val exifDegree: Int

                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    exifDegree = exifOrientationToDegrees(exifOrientation)

                    Glide.with(this).load(rotate(bitmap, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(imgProfile)

                }
                REQUEST_IMAGE_GALLERY -> {
                    try {
                        val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
                        val img = BitmapFactory.decodeStream(inputStream)
                        val out = FileOutputStream(imageFilePath)

                        img.compress(Bitmap.CompressFormat.JPEG, 90, out)
                        out.close()
                        inputStream?.close()

                        Glide.with(this).load(img).apply(RequestOptions().circleCrop()).into(imgProfile)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
