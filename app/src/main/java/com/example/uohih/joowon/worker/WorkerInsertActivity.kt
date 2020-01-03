package com.example.uohih.joowon.worker

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
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
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.database.DBHelper
import com.example.uohih.joowon.view.CalendarDialog
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.DialogListAdapter
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.main.MainListActivity
import com.example.uohih.joowon.view.CustomDialog
import com.example.uohih.joowon.view.CustomListDialog
import kotlinx.android.synthetic.main.activity_worker_insert.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 직원추가
 */
class WorkerInsertActivity : JWBaseActivity(), View.OnFocusChangeListener, TextView.OnEditorActionListener, View.OnClickListener {

    private val base = JWBaseApplication()
    private val dbHelper = DBHelper(this)
    private val todayJson = getToday()
    private var imageFilePath: String = ""
    private lateinit var name: String
    private lateinit var phoneNum: String
    private lateinit var use: String
    private lateinit var total: String
    private lateinit var joinDate: String
    private lateinit var no: String

    private var workerUpdate: String = "N" //변경하기
    private var workerBundle = Bundle()

    private lateinit var customDialog: CustomDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_insert)
        customDialog = CustomDialog(this, android.R.style.Theme_Material_Dialog_MinWidth)

        if (intent.hasExtra("workerUpdate")) {
            workerUpdate = intent.getStringExtra("workerUpdate")
        }

        if ("Y" == workerUpdate) {
            worker_insert_title_view.setBackBtn()

            workerBundle = intent.getBundleExtra("workerBundle")
            name = workerBundle.getString("name", "")                     //이름
            imageFilePath = workerBundle.getString("picture", "")         //사진
            phoneNum = workerBundle.getString("phone", "")                //핸드폰번호
            use = workerBundle.getString("use", "0")                      //사용한 휴가일수
            total = workerBundle.getString("total", "15")                 //잔여 휴가일수
            joinDate = workerBundle.getString("joinDate", "")             //입사날짜
            no = workerBundle.getString("no", "0")

            worker_layout_delete.visibility = View.VISIBLE
            worker_insert_layout_use.visibility = View.VISIBLE
            worker_insert_btn_bottom.text = getString(R.string.workerUpdate_update)


            if (imageFilePath != "") {
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

                Glide.with(this).load(rotate(bitmap, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(worker_img)

            }

            worker_insert_edt_name.hint = name
            worker_insert_edt_name.setText(name)
            worker_insert_edt_phone.hint = phoneNum
            worker_insert_edt_phone.setText(phoneNum)
            worker_insert_edt_join.text = joinDate
            worker_insert_edt_vacation.hint = total
            worker_insert_edt_vacation.setText(total)
            worker_insert_edt_use.hint = use
            worker_insert_edt_use.setText(use)


        }


        //이름
        worker_insert_edt_name.onFocusChangeListener = this
        worker_insert_edt_name.setOnEditorActionListener(this)
        worker_insert_edt_name.addTextChangedListener(editTextWatcher(worker_insert_edt_name))

        //핸드폰번호
        worker_insert_edt_phone.onFocusChangeListener = this
        worker_insert_edt_phone.setOnEditorActionListener(this)
        worker_insert_edt_phone.addTextChangedListener(editTextWatcher(worker_insert_edt_phone))
        worker_insert_edt_phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        //입사날짜
        worker_insert_edt_join.onFocusChangeListener = this
        worker_insert_edt_join.isFocusableInTouchMode = true
        worker_insert_edt_join.isFocusable = true
        worker_insert_edt_join.setOnClickListener(this)
        worker_insert_edt_join.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(joinDate, "$1-$2-$3")


        //총휴가개수
        worker_insert_edt_vacation.onFocusChangeListener = this
        worker_insert_edt_vacation.setOnEditorActionListener(this)
        worker_insert_edt_vacation.addTextChangedListener(editTextWatcher(worker_insert_edt_vacation))

        //사용한휴가개수
        worker_insert_edt_use.setOnEditorActionListener(this)
        worker_insert_edt_use.onFocusChangeListener = this
        worker_insert_edt_use.addTextChangedListener(editTextWatcher(worker_insert_edt_use))


        //EditText 삭제 버튼
        worker_insert_btn_delete1.setOnClickListener(this)
        worker_insert_btn_delete2.setOnClickListener(this)
        worker_insert_btn_delete3.setOnClickListener(this)
        worker_insert_btn_delete4.setOnClickListener(this)

        //등록 및 변경 버튼
        worker_insert_btn_bottom.setOnClickListener(this)

        //프로필 사진 등록
        worker_insert_plus.setOnClickListener(this)

        //삭제하기
        worker_layout_delete.setOnClickListener(this)

    }


    /**
     * editTextWatcher
     */
    private inner class editTextWatcher(private val mEditText: EditText) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
            when (mEditText) {
                worker_insert_edt_name -> { //이름
                    if (count == 0 && start == 0) worker_insert_btn_delete1.visibility = View.GONE
                    else worker_insert_btn_delete1.visibility = View.VISIBLE

                }
                worker_insert_edt_phone -> { //핸드폰번호
                    if (count == 0 && start == 0) worker_insert_btn_delete2.visibility = View.GONE
                    else worker_insert_btn_delete2.visibility = View.VISIBLE

                }
                worker_insert_edt_vacation -> { //총휴가개수
                    if (count == 0 && start == 0) worker_insert_btn_delete3.visibility = View.GONE
                    else worker_insert_btn_delete3.visibility = View.VISIBLE
                }
                worker_insert_edt_use -> {  //사용휴가개수
                    if (count == 0 && start == 0) worker_insert_btn_delete4.visibility = View.GONE
                    else worker_insert_btn_delete4.visibility = View.VISIBLE
                }
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }


    /**
     * onEditorAction
     */
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            when (v) {
                worker_insert_edt_name -> worker_insert_edt_join.requestFocus()
                worker_insert_edt_phone -> worker_insert_edt_vacation.requestFocus()
                worker_insert_edt_vacation -> {
                    if ("Y" == workerUpdate) worker_insert_edt_use.requestFocus()
                    else varification()
                }
                worker_insert_edt_use -> varification()
            }
        }
        return false
    }


    /**
     * onFocusChange
     */
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v) {
            worker_insert_edt_name -> { //이름
                if (hasFocus) {
                    if (worker_insert_edt_name.text.isNotEmpty())
                        worker_insert_btn_delete1.visibility = View.VISIBLE
                } else {
                    worker_insert_btn_delete1.visibility = View.GONE
                }
            }
            worker_insert_edt_phone -> { //핸드폰번호
                if (hasFocus) {
                    if (worker_insert_edt_phone.text.isNotEmpty())
                        worker_insert_btn_delete2.visibility = View.VISIBLE
                } else {
                    worker_insert_btn_delete2.visibility = View.GONE
                }
            }
            worker_insert_edt_join -> { //입사일자
                if (hasFocus) showCalendarDialog()
            }
            worker_insert_edt_vacation -> { //총휴가개수
                if (hasFocus) {
                    if (worker_insert_edt_vacation.text.isNotEmpty())
                        worker_insert_btn_delete3.visibility = View.VISIBLE
                } else {
                    worker_insert_btn_delete3.visibility = View.GONE
                }
            }
            worker_insert_edt_use -> { //사용휴가개수
                if (hasFocus) {
                    if (worker_insert_edt_use.text.isNotEmpty())
                        worker_insert_btn_delete4.visibility = View.VISIBLE
                } else {
                    worker_insert_btn_delete4.visibility = View.GONE
                }
            }
        }
    }


    /**
     * onClick
     */
    override fun onClick(v: View?) {
        when (v) {
            worker_insert_btn_delete1 -> worker_insert_edt_name.setText("")
            worker_insert_btn_delete2 -> worker_insert_edt_phone.setText("")
            worker_insert_btn_delete3 -> worker_insert_edt_vacation.setText("")
            worker_insert_btn_delete4 -> worker_insert_edt_use.setText("")
            worker_insert_edt_join -> showCalendarDialog()
            worker_insert_btn_bottom -> varification()
            worker_insert_plus -> {
                val listViewAdapter = DialogListAdapter(this, ArrayList())
                listViewAdapter.setContent(getString(R.string.menu01))
                listViewAdapter.setContent(getString(R.string.menu02))

                var customDialogList = CustomListDialog(this, android.R.style.Theme_Material_Dialog_MinWidth)
                customDialogList = customDialogList.showDialogList(this, getString(R.string.workerInsert_picture), DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int -> }, listViewAdapter, AdapterView.OnItemClickListener { parent, view, p, id ->
                    when (p) {
                        0 -> sendTakePhotoIntent()      //카메라
                        1 -> sendTakeGalleryIntent()    //갤러리
                    }
                    customDialogList.dismiss()
                })!!
                customDialogList.show()
            }
            worker_layout_delete -> {
                customDialog.showDialog(this, String.format(resources.getString(R.string.workerUpdate_delete_msg), name),
                        resources.getString(R.string.btn02), null,
                        resources.getString(R.string.btn01), DialogInterface.OnClickListener { dialog, which ->
                    dbHelper.delete(dbHelper.tableNameWorkerJW, no)


                    val intent = Intent(this, MainListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                })
            }
        }

    }


    /**
     * 키보드 숨기기
     * edt: EditText
     */
    private fun hideKeypad(edt: EditText) {
        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt.windowToken, 0)
        worker_insert_btn_bottom.requestFocus()
    }

    /**
     * 검증
     */
    private fun varification() {
        // 이름
        if (worker_insert_edt_name.text.toString().isEmpty()) {
            customDialog.showDialog(this, resources.getString(R.string.workerInsert_name2), resources.getString(R.string.btn01), null)
            worker_insert_edt_name.requestFocus()
            return
        }
        // 핸드폰 번호
        if (worker_insert_edt_phone.text.toString().length in 1..9) {
            customDialog.showDialog(this, resources.getString(R.string.workerInsert_phone3), resources.getString(R.string.btn01), null)
            worker_insert_edt_phone.requestFocus()
            return
        }
        // 총휴가
        if (worker_insert_edt_vacation.text.toString().isEmpty()) {
            customDialog.showDialog(this, resources.getString(R.string.workerInsert_vacation2), resources.getString(R.string.btn01), null)
            worker_insert_edt_vacation.requestFocus()
            return
        }
        // 사용휴가
        if ("Y" == workerUpdate && worker_insert_edt_use.text.toString().isEmpty()) {
            customDialog.showDialog(this, resources.getString(R.string.workerInsert_use2), resources.getString(R.string.btn01), null)
            worker_insert_edt_vacation.requestFocus()
            return
        }

        name = worker_insert_edt_name.text.toString()
        joinDate = worker_insert_edt_join.text.toString().replace("-", "")
        phoneNum = worker_insert_edt_phone.text.toString().replace("-", "")
        total = worker_insert_edt_vacation.text.toString()
        use = worker_insert_edt_use.text.toString()

        if ("Y" == workerUpdate) {
            dbHelper.update(dbHelper.tableNameWorkerJW, name, joinDate, phoneNum, use, total, imageFilePath, no)
        } else {
//            dbHelper.createTable(worker_insert_edt_name.text.toString() + "_" + phoneNum)
            dbHelper.insert(dbHelper.tableNameWorkerJW, "name", "joinDate", "phone", "use", "total", "picture", name, joinDate, phoneNum, "0", total, imageFilePath)
        }

        finish()

    }

    /**
     * 캘린더 다이얼로그
     */
    private fun showCalendarDialog() {
        var calendarDialog = CalendarDialog(this, android.R.style.Theme_Material_Dialog_MinWidth)
        val date = worker_insert_edt_join.text.toString()
        calendarDialog = calendarDialog.showDialogCalendar(this, date)!!
        calendarDialog.show()
        calendarDialog.setOnDismissListener {
            worker_insert_edt_join.text = (Constants.YYYYMMDD_PATTERN).toRegex().replace(base.getSeleteDate(), "$1-$2-$3")
        }
    }


    /**
     * 사진 이미지 생성
     * return File
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "TEST_" + timeStamp + "_"
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
                // Error occurred while creating the File
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
            // Error occurred while creating the File
        }

        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this, packageName, photoFile)
        }
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

//                    (findViewById<ImageView>(R.id.worker_img)).setImageBitmap(rotate(bitmap, exifDegree.toFloat()))
                    Glide.with(this).load(rotate(bitmap, exifDegree.toFloat())).apply(RequestOptions().circleCrop()).into(worker_img)
//                    worker_img_plus.visibility = View.GONE
                }
                REQUEST_IMAGE_GALLERY -> {
                    try {
                        val inputStream = contentResolver.openInputStream(data?.data)
                        val img = BitmapFactory.decodeStream(inputStream)
                        val out = FileOutputStream(imageFilePath)

                        img.compress(Bitmap.CompressFormat.JPEG, 90, out)

                        out.close()

                        inputStream.close()
//                        (findViewById<ImageView>(R.id.worker_img)).setImageBitmap(img)
                        Glide.with(this).load(img).apply(RequestOptions().circleCrop()).into(worker_img)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
