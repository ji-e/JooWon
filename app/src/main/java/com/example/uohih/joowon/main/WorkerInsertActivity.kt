package com.example.uohih.joowon.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.uohih.dailylog.base.JWBaseActivity
import com.example.uohih.dailylog.base.JWBaseApplication
import com.example.uohih.dailylog.database.DBHelper
import com.example.uohih.dailylog.view.CalendarDialog
import com.example.uohih.joowon.R
import com.example.uohih.joowon.base.LogUtil
import kotlinx.android.synthetic.main.activity_worker_insert.*

class WorkerInsertActivity : Activity() {
    private val base = JWBaseApplication()
    private val dbHelper = DBHelper(this)
    private val todayJson = JWBaseActivity().getToday()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_insert)


        /**
         * 이름 입력에 따른 삭제 버튼 --------------------------------------------------------------
         */
        worker_insert_edt_name.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (worker_insert_edt_name.text.isNotEmpty())
                    worker_insert_btn_delete1.visibility = View.VISIBLE
            } else {
                worker_insert_btn_delete1.visibility = View.GONE
            }

        }

        /**
         * 이름 입력 다음
         */
        worker_insert_edt_name.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                worker_insert_edt_join.requestFocus()
            }
            false
        }

        /**
         * 이름 입력에 따른 삭제 버튼
         */
        worker_insert_edt_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 0 && start == 0) {
                    worker_insert_btn_delete1.visibility = View.GONE
                } else {
                    worker_insert_btn_delete1.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        /**
         * 삭제 버튼 클릭 리스너 1 -------------------------------------------------------------------
         */
        worker_insert_btn_delete1.setOnClickListener {
            worker_insert_edt_name.setText("")
        }


        /**
         * 핸드폰 번호 입력에 따른 삭제 버튼 ---------------------------------------------------------
         */
        worker_insert_edt_phone.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (worker_insert_edt_phone.text.isNotEmpty())
                    worker_insert_btn_delete2.visibility = View.VISIBLE
            } else {
                worker_insert_btn_delete2.visibility = View.GONE
            }

        }

        /**
         * 핸드폰 번호 입력 다음
         */
        worker_insert_edt_phone.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                worker_insert_edt_vacation.requestFocus()
            }
            false
        }

        /**
         * 핸드폰 번호 입력에 따른 삭제 버튼
         */
        worker_insert_edt_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 0 && start == 0) {
                    worker_insert_btn_delete2.visibility = View.GONE
                } else {
                    worker_insert_btn_delete2.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        /**
         * 삭제 버튼 클릭 리스너 2 -------------------------------------------------------------------
         */
        worker_insert_btn_delete2.setOnClickListener {
            worker_insert_edt_phone.setText("")
        }

        /**
         * 입사 날짜 입력
         */

        worker_insert_edt_join.isFocusableInTouchMode = true
        worker_insert_edt_join.isFocusable = true

        worker_insert_edt_join.text = todayJson.get("year").toString() + "-" + todayJson.get("month").toString() + "-" + todayJson.get("date").toString()

        worker_insert_edt_join.setOnClickListener {
            showCalendarDialog()
        }

        worker_insert_edt_join.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showCalendarDialog()
            }
        }


        /**
         * 휴가 입력에 따른 삭제 버튼 ---------------------------------------------------------
         */
        worker_insert_edt_vacation.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (worker_insert_edt_vacation.text.isNotEmpty())
                    worker_insert_btn_delete3.visibility = View.VISIBLE
            } else {
                worker_insert_btn_delete3.visibility = View.GONE
            }

        }

        /**
         * 휴가 입력 다음
         */
        worker_insert_edt_vacation.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                worker_insert_btn_bottom.requestFocus()
            }
            false
        }

        /**
         * 휴가 입력에 따른 삭제 버튼
         */
        worker_insert_edt_vacation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 0 && start == 0) {
                    worker_insert_btn_delete3.visibility = View.GONE
                } else {
                    worker_insert_btn_delete3.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        /**
         * 삭제 버튼 클릭 리스너 3 -------------------------------------------------------------------
         */
        worker_insert_btn_delete3.setOnClickListener {
            worker_insert_edt_vacation.setText("")
        }


        /**
         * 확인 버튼 클릭 이벤트
         */
        worker_insert_btn_bottom.setOnClickListener {
            var date = worker_insert_edt_join.toString().substring(0, 4) + base.getSeleteDate().substring(5, 7) + base.getSeleteDate().substring(8)
            dbHelper.createTable(worker_insert_edt_name.text.toString() + "_" + worker_insert_edt_phone.text.toString())
            dbHelper.insert(dbHelper.tableNameJW, "name", "joinDate", "phone", "use", "total", worker_insert_edt_name.text.toString(), date, worker_insert_edt_phone.text.toString(), "0", worker_insert_edt_vacation.text.toString())

            finish()
        }
    }

    /**
     * 키보드 숨기기
     */
    private fun hideKeypad(edt: EditText) {
        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edt.windowToken, 0)
        worker_insert_btn_bottom.requestFocus()
    }

    /**
     * 캘린더 다이얼로그
     */

    private fun showCalendarDialog() {
        var calendarDialog = CalendarDialog(this, android.R.style.Theme_Material_Dialog_MinWidth)
        var date = worker_insert_edt_join.text.toString()
        calendarDialog = calendarDialog.showDialogCalendar(this, date)!!
        calendarDialog.show()
        calendarDialog.setOnDismissListener {
            worker_insert_edt_join.text = base.getSeleteDate().substring(0, 4) + "-" + base.getSeleteDate().substring(4, 6) + "-" + base.getSeleteDate().substring(6)
        }


    }

}
