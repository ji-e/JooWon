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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
import com.example.uohih.joowon.ui.customView.CalendarDialog
import com.example.uohih.joowon.ui.customView.CustomDialog
import com.example.uohih.joowon.ui.customView.CustomListDialog
import com.example.uohih.joowon.util.DateCommonUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_worker_insert.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

/**
 * 직원추가
 */
class WorkerInsertActivity : JWBaseActivity() {
    private val thisActivity by lazy { this }

    private lateinit var workerViewModel: WorkerViewModel
    private lateinit var binding: ActivityWorkerInsertBinding

    private val base = JWBaseApplication()
    private val dbHelper = DBHelper(this)
    private val todayJSONObject = DateCommonUtil().getToday()
    private var imageFilePath: String = ""
    private lateinit var name: String
    private lateinit var phoneNum: String
    private lateinit var use: String
    private lateinit var total: String
    private lateinit var joinDate: String
    private lateinit var no: String

    private var workerUpdate: String = "N" //변경하기
    private var workerBundle = Bundle()


    private lateinit var layProfile: ConstraintLayout
    private lateinit var imgProfile: ImageView
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtTotalCnt: EditText
    private lateinit var tvBirthDate: TextView
    private lateinit var tvEnjoyDate: TextView
    private lateinit var btnNameDelete: ImageButton
    private lateinit var btnPhoneDelete: ImageButton

    private val customDialog by lazy {
        CustomDialog(this, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("workerUpdate")) {
            workerUpdate = intent.getStringExtra("workerUpdate")
        }


        binding = DataBindingUtil.setContentView<ActivityWorkerInsertBinding>(thisActivity, R.layout.activity_worker_insert)
        binding.run {
            workerViewModel = ViewModelProviders.of(thisActivity, WorkerViewModelFactory()).get(WorkerViewModel::class.java)
            lifecycleOwner = thisActivity
            workerInsertVm = workerViewModel
        }


        initView()

    }

    private fun initView() {
//        if ("Y" == workerUpdate) {
//            workerInsert_titleView.setBackBtn()
//
//            workerBundle = intent.getBundleExtra("workerBundle")
//            name = workerBundle.getString("name", "")                     //이름
//            imageFilePath = workerBundle.getString("picture", "")         //사진
//            phoneNum = workerBundle.getString("phone", "")                //핸드폰번호
//            use = workerBundle.getString("use", "0")                      //사용한 휴가일수
//            total = workerBundle.getString("total", "15")                 //잔여 휴가일수
//            joinDate = workerBundle.getString("joinDate", "")             //입사날짜
//            no = workerBundle.getString("no", "0")
//
//            worker_layout_delete.visibility = View.VISIBLE
//            worker_insert_layout_use.visibility = View.VISIBLE
//            workerInsert_btnConfirm.text = getString(R.string.workerUpdate_update)
//
//
//            if (imageFilePath != "") {
//                val bitmap = BitmapFactory.decodeFile(imageFilePath)
//                lateinit var exif: ExifInterface
//
//                try {
//                    exif = ExifInterface(imageFilePath)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//                val exifOrientation: Int
//                val exifDegree: Int
//
//                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//                exifDegree = exifOrientationToDegrees(exifOrientation)
//
//                Glide.with(this).load(rotate(bitmap, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(worker_img)
//
//            }
//
//            worker_insert_edt_name.hint = name
//            worker_insert_edt_name.setText(name)
//            worker_insert_edt_phone.hint = phoneNum
//            worker_insert_edt_phone.setText(phoneNum)
//            worker_insert_edt_join.text = joinDate
//            worker_insert_edt_vacation.hint = total
//            worker_insert_edt_vacation.setText(total)
//            worker_insert_edt_use.hint = use
//            worker_insert_edt_use.setText(use)
//
//
//        }

        layProfile = binding.workerInsertLayProfileImg
        imgProfile = binding.workerInsertImgProfileImg
        edtName = binding.workerInsertEdtName
        edtPhone = binding.workerInsertEdtPhone
        edtTotalCnt = binding.workerInsertEdtTotalCnt
        tvBirthDate = binding.workerInsertTvBirthDate
        tvEnjoyDate = binding.workerInsertTvEnjoyDate
        btnNameDelete = binding.workerInsertBtnNameDelete
        btnPhoneDelete = binding.workerInsertBtnPhoneDelete

        tvEnjoyDate.isFocusableInTouchMode = true

        edtName.addTextChangedListener(WorkerInsertTextWatcher(edtName))
        edtPhone.addTextChangedListener(WorkerInsertTextWatcher(edtPhone))
        edtPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        edtName.onFocusChangeListener = WorkerInsertFocusChangeListner()
        edtPhone.onFocusChangeListener = WorkerInsertFocusChangeListner()
        tvBirthDate.onFocusChangeListener = WorkerInsertFocusChangeListner()
        tvEnjoyDate.onFocusChangeListener = WorkerInsertFocusChangeListner()

        edtTotalCnt.setOnEditorActionListener(WorkerInsertEditActionListener())

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

            if ("failure" == jw3002Data.result) {

            } else {
                if ("Y" == jw3002Data.resbody?.addEmployeeValid) {
                    customDialog.showDialog(
                            thisActivity,
                            getString(R.string.workerInsert_dialog_msg),
                            getString(R.string.btnConfirm),
                            DialogInterface.OnClickListener { dialog, which ->
                                thisActivity.finish()
                            })
                }
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
    private inner class WorkerInsertFocusChangeListner : View.OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hasFocus) {
                when (v) {
                    edtName -> btnNameDelete.visibility =
                            if (edtName.text.isNotEmpty()) View.VISIBLE
                            else View.GONE
                    edtPhone -> btnPhoneDelete.visibility =
                            if (edtPhone.text.isNotEmpty()) View.VISIBLE
                            else View.GONE
                    tvBirthDate -> showCalendarDialog(tvBirthDate)
                    tvEnjoyDate -> showCalendarDialog(tvEnjoyDate)
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
                showCalendarDialog(tvBirthDate)
            }
            tvEnjoyDate, binding.workerInsertBtnEnjoyCalendar -> {
                showCalendarDialog(tvEnjoyDate)
            }
            binding.workerInsertBtnConfirm -> {
                addEmployee()
            }
            layProfile -> {
                showListDialog()
            }
//            worker_layout_delete -> {
//                customDialog.showDialog(this, String.format(resources.getString(R.string.workerUpdate_delete_msg), name),
//                        resources.getString(R.string.btnCancel), null,
//                        resources.getString(R.string.btnConfirm), DialogInterface.OnClickListener { dialog, which ->
//                    dbHelper.delete(dbHelper.tableNameWorkerJW, no)
//
//
//                    val intent = Intent(this, MainListActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    startActivity(intent)
//
//                })
//            }
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
     * 키보드 숨기기
     * edt: EditText
     */
    private fun hideKeypad(edt: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt.windowToken, 0)
        workerInsert_btnConfirm.requestFocus()
    }

    /**
     * 리스트 다이얼로그
     */
    private fun showListDialog() {
        val listViewAdapter = DialogListAdapter(thisActivity, ArrayList()).apply {
            setContent(getString(R.string.menu01))
            setContent(getString(R.string.menu02))
        }

        val customDialogList = CustomListDialog(thisActivity, android.R.style.Theme_Material_Dialog_MinWidth)
        customDialogList.showDialogList(
                thisActivity,
                getString(R.string.workerInsert_picture),
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int -> },
                listViewAdapter,
                AdapterView.OnItemClickListener { parent, view, p, id ->
                    when (p) {
                        0 -> sendTakePhotoIntent()      //카메라
                        1 -> sendTakeGalleryIntent()    //갤러리
                    }
                    customDialogList.dismiss()
                })?.show()

    }

    /**
     * 캘린더 다이얼로그
     */
    private fun showCalendarDialog(textView: TextView) {
        val date = textView.text.toString()
//        val calendarDialog = CalendarDialog(thisActivity, android.R.style.Theme_Material_Dialog_MinWidth)
//
//        calendarDialog.createDialogCalendar(thisActivity, date)?.apply {
//            setOnDismissListener {
//                textView.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(base.getSelectDate(), "$1-$2-$3")
//            }
//        }?.show()
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
