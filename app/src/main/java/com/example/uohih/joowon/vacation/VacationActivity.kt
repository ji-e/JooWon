package com.example.uohih.joowon.vacation

import VacationSearchAdapter
import VacationSearchAdapter.VacationSearchListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.uohih.joowon.R
import com.example.uohih.joowon.adapter.StaffData
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.database.DBHelper
import kotlinx.android.synthetic.main.activity_vacation.*


class VacationActivity : JWBaseActivity() {

    /* // build the ContactChip list
     private var contactList = arrayListOf<WorkerVacationChip>()

     // get the list
     val contactsSelected by lazy { worker_vacation_edt_name.selectedChipList as List<WorkerVacationChip> }*/


    // 리스트 뷰
    var mainList = arrayListOf<StaffData>()
    var subList = arrayListOf<StaffData>()

    val mVacationSearchAdapter by lazy {
        VacationSearchAdapter(this, subList)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation)

        setData()


        /*// pass the ContactChip list
        worker_vacation_edt_name.filterableList = contactList




        worker_vacation_edt_name.addChipsListener(object : ChipsListener {
            override fun onChipAdded(chip: ChipInterface, newSize: Int) {

            }

            override fun onChipRemoved(chip: ChipInterface, newSize: Int) {

            }

            override fun onTextChanged(text: CharSequence) {

            }
        })

//        worker_vacation_edt_name.ava(uri);


        worker_vacation_btn_search.setOnClickListener(View.OnClickListener {
            var listString = ""
            for (chip in worker_vacation_edt_name.selectedChipList) {
                listString += chip.getLabel().toString() + " (" + (if (chip.getInfo() != null) chip.getInfo() else "") + ")" + ", "
            }
            LogUtil.e(listString)
//            mChipListText.setText(listString)
        })*/


        vacation_edt_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                setSearch(vacation_edt_name.text.toString())
            }
        })

        vacation_listview.adapter = mVacationSearchAdapter
        mVacationSearchAdapter.setmVacationSearchListener(object : VacationSearchListener {
            override fun onClickItem(position: Int) {
                subList[position]
            }
        })

    }

    override fun onResume() {
        super.onResume()

        setData()
    }


    /**
     * db에서 데이터 가져온 후 set
     */
    private fun setData() {
        val dbHelper = DBHelper(this)
        val cursor = dbHelper.selectAll(dbHelper.tableNameWorkerJW)

        while (cursor.moveToNext()) {
            mainList.add(StaffData(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)))
        }
    }


    /**
     * 검색한 후 set
     */
    private fun setSearch(charText: String) { // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        subList.clear()
        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        // 리스트의 모든 데이터를 검색한다.
        for (i in 0 until mainList.size) { // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
            if (mainList[i].name.toLowerCase().contains(charText) || mainList[i].phone.toLowerCase().contains(charText)) { // 검색된 데이터를 리스트에 추가한다.
                subList.add(mainList[i])
            }
        }

        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        mVacationSearchAdapter.notifyDataSetChanged()
    }


}

