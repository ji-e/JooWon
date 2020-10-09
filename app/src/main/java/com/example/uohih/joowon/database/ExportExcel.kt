package com.example.uohih.joowon.database

import android.content.Context
import android.widget.Toast
import com.example.uohih.joowon.base.JWBaseActivity
import com.example.uohih.joowon.base.LogUtil
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


/**
 * 엑셀파일 만들기
 * mContext: Context
 */
class ExportExcel(mContext: Context) {

    private val mContext = mContext
    // 파일명
    private val fileName = "joowon.xlsx"
    // 파일 위치
    private val fileDir = "/mnt/sdcard/Download"
    // 파일 총 명
    private val totalName = "$fileDir/$fileName"

    private val db = DBHelper(mContext)
    private var dataListWorker = ArrayList<WorkerData>()
    private var dataListVacation = ArrayList<VacationData>()
    private val workerColumns = arrayOf("NO", "NAME", "JOINDATE", "PHONE", "USE", "TOTAL", "PICTURE")
    private val vacationColumns = arrayOf("NO", "NAME", "PHONE", "DATE", "CONTENT", "USE", "TOTAL")
    private val workbook: XSSFWorkbook = XSSFWorkbook()
    private val workerSheet = workbook.createSheet("worker_sheet")
    private val vacationSheet = workbook.createSheet("vacation_sheet")
    private val createHelper = workbook.creationHelper


    /**
     * 엑셀 파일 존재 여부 확인
     * fileName: String: 파일명, type: Boolean: 퍼미션 체크 확인?
     * return Boolean
     * true: 있음, false: 없음
     */
    fun fileExists(type: Boolean): Boolean {
//        val fileDir = "/mnt/sdcard/Download"
//        val totalName = "$fileDir/$fileName"

        val file = File(totalName)
        if (file.exists() && !type) {
            file.delete()
            return true
        } else if (file.exists() && type) {
            return true
        }
        return false
    }


    /**
     * 엑셀파일 만들기
     */
    fun makeExcelFile() {

        // 파일 존재 확인
        fileExists(false)

        val headerFont = workbook.createFont()
        headerFont.bold = true
        headerFont.boldweight = (Font.BOLDWEIGHT_BOLD)
        headerFont.color = IndexedColors.BLUE_GREY.getIndex()

        val headerCellStyle = workbook.createCellStyle()
        headerCellStyle.setFont(headerFont)

        val workerHeaderRow = workerSheet.createRow(0)


        for (i in workerColumns.indices) {
            val cell = workerHeaderRow.createCell(i)
            cell.setCellValue(workerColumns[i])
            cell.cellStyle = headerCellStyle
        }

//        insertSheetData("worker")

        val vacationHeaderRow = vacationSheet.createRow(0)
        for (i in vacationColumns.indices) {
            val cell = vacationHeaderRow.createCell(i)
            cell.setCellValue(vacationColumns[i])
            cell.cellStyle = headerCellStyle
        }
        // 엑셀시트에 데이터 삽입
        insertSheetData()

    }

    /**
     * 엑셀시트에 데이터 삽입
     */
    fun insertSheetData() {
        // DB 데이터 List로 가져오기
        SQLiteToList()

        val asyncExecutor = AsyncExecutor(mContext)
        asyncExecutor.setAsyncCallback(object : AsyncCallback {
            override fun onPostExecute(any:Any?) {
                writeExcelFile()
            }

            override fun doInBackground() {
                val dateCellStyle = workbook.createCellStyle()
                dateCellStyle.dataFormat = createHelper.createDataFormat().getFormat("yyyy-mm-dd")

//        if (type == "worker") {
                var rowNum = 1
                for (data in dataListWorker) {
                    val row = workerSheet.createRow(rowNum++)

                    row.createCell(0).setCellValue(data.no.toString())               //no
                    val dateCell = row.createCell(1)                      //name
                    dateCell.setCellValue(data.name)
                    dateCell.cellStyle = dateCellStyle
                    row.createCell(2).setCellValue(data.joinDate.toString())        //joinDate
                    row.createCell(3).setCellValue(data.phone)                      //phone
                    row.createCell(4).setCellValue(data.use.toString())             //use
                    row.createCell(5).setCellValue(data.total.toString())           //total
                    row.createCell(6).setCellValue(data.picture)                    //picture
                }
                workerSheet.setColumnWidth(1, 3000)
                workerSheet.setColumnWidth(2, 4000)
//        } else {
                rowNum = 1
                for (data in dataListVacation) {
                    val row = vacationSheet.createRow(rowNum++)

                    row.createCell(0).setCellValue(data.no.toString())               //no
                    val dateCell = row.createCell(1)                      //name
                    dateCell.setCellValue(data.name)
                    dateCell.cellStyle = dateCellStyle
                    row.createCell(2).setCellValue(data.phone)                      //phone
                    row.createCell(3).setCellValue(data.date.toString())            //date
                    row.createCell(4).setCellValue(data.content)                    //content
                    row.createCell(5).setCellValue(data.use.toString())             //use
                    row.createCell(6).setCellValue(data.total.toString())           //total
                }
//            vacationSheet.setColumnWidth(1, 3000)
//            vacationSheet.setColumnWidth(3, 4000)
//            vacationSheet.setColumnWidth(4, 5000)
//        }


//        for (i in 0 until columns.size) {
//            sheet.autoSizeColumn(i)
//        }
            }
        })
        asyncExecutor.execute("excel")

    }


    /**
     * 엑셀 파일 쓰기
     */
    fun writeExcelFile() {
        val fileout = FileOutputStream(totalName)
        workbook.write(fileout)
        fileout.close()
        Toast.makeText(mContext, "엑셀로 내보내기가 완료되었습니다.", Toast.LENGTH_SHORT).show()
//        JWBaseActivity().hideLoading()
    }

    /**
     * 엑셀 파일 읽기
     */
    fun readExcelFile() {
        val asyncExecutor = AsyncExecutor(mContext)
        asyncExecutor.setAsyncCallback(object : AsyncCallback {
            override fun onPostExecute(any: Any?) {
                Toast.makeText(mContext, "엑셀 가져오기가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun doInBackground() {
                if (!fileExists(true)) {
                    return
                }

                val excelFile = FileInputStream(File(totalName))
                val workbook = XSSFWorkbook(excelFile)
                var sheet = workbook.getSheet("worker_sheet")

                for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                    val row = sheet.getRow(rowIndex)
                    if (row != null) {
                        var no = ""
                        var name = ""
                        var joinDate = ""
                        var phone = ""
                        var use = "0"
                        var total = "15"
                        var picture = ""
                        for (columnIndex in 0 until row.physicalNumberOfCells) {
                            val cell = row.getCell(columnIndex)
                            if (cell != null) {
                                when (columnIndex) {
                                    0 -> no = cell.stringCellValue
                                    1 -> name = cell.stringCellValue
                                    2 -> joinDate = cell.stringCellValue
                                    3 -> phone = cell.stringCellValue
                                    4 -> use = cell.stringCellValue
                                    5 -> total = cell.stringCellValue
                                    6 -> picture = cell.stringCellValue
                                }
                            }
                        }
                        dataListWorker.add(WorkerData(no.toInt(), name, joinDate.toInt(), phone, use.toDouble(), total.toDouble(), picture))
                        LogUtil.d(dataListWorker)
                    }
                }

//        sheet = workbook.getSheet("vacation_sheet")
//        for (rowIndex in 1 until sheet.physicalNumberOfRows) {
//            val row = sheet.getRow(rowIndex)
//            if (row != null) {
//                var no = ""
//                var name = ""
//                var phone = ""
//                var date = ""
//                var content = ""
//                var use = "0"
//                var total = "15"
//                for (columnIndex in 0 until row.physicalNumberOfCells) {
//                    val cell = row.getCell(columnIndex)
//                    if (cell != null) {
//                        when (columnIndex) {
//                            0 -> no = cell.stringCellValue
//                            1 -> name = cell.stringCellValue
//                            2 -> phone = cell.stringCellValue
//                            3 -> date = cell.stringCellValue
//                            4 -> content = cell.stringCellValue
//                            5 -> use = cell.stringCellValue
//                            6 -> total = cell.stringCellValue
//                        }
//                    }
//                }
//                dataListVacation.add(VacationData(no.toInt(), name, phone, date.toInt(), content, use.toInt(), total.toInt()))
//                LogUtil.d(dataListVacation)
//            }
//        }
                excelFile.close()

                db.onReset()

                for (i in 0 until dataListWorker.size) {
                    db.insert(db.tableNameWorkerJW, "name", "joinDate", "phone", "use", "total", "picture",
                            dataListWorker[i].name, dataListWorker[i].joinDate.toString(), dataListWorker[i].phone, dataListWorker[i].use.toString(), dataListWorker[i].total.toString(), dataListWorker[i].picture)
                }

                for (i in 0 until dataListVacation.size) {
                    db.insert(db.tableNameVacationJW, "name", "phone", "date", "content", "use", "total",
                            dataListVacation[i].name, dataListVacation[i].phone, dataListVacation[i].date.toString(), dataListVacation[i].content, dataListVacation[i].use.toString(), dataListVacation[i].total.toString())
                }

            }
        })

        asyncExecutor.execute("excel")


    }

    /**
     * SQLite DB 데이터 리스트로 가져오기
     */
    fun SQLiteToList() {
        var cursor = db.selectAll(db.tableNameWorkerJW)
        dataListWorker.clear()
        while (cursor.moveToNext()) {
            if (cursor.getString(1) != null) {
                LogUtil.d(cursor.getString(0) + "  :   " + cursor.getString(1) + "  :   " + cursor.getString(2) + "  :   "
                        + cursor.getString(3) + "  :   " + cursor.getString(4) + "  :   " + cursor.getString(5) + "  :   " + cursor.getString(6))
                dataListWorker.add(WorkerData(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6)))
            }
        }

        cursor = db.selectAll(db.tableNameVacationJW)
        dataListVacation.clear()
        while (cursor.moveToNext()) {
            if (cursor.getString(1) != null) {
                LogUtil.d(cursor.getString(0) + "  :   " + cursor.getString(1) + "  :   " + cursor.getString(2) + "  :   "
                        + cursor.getString(3) + "  :   " + cursor.getString(4) + "  :   " + cursor.getString(5) + "  :   " + cursor.getString(6))
                dataListVacation.add(VacationData(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getString(4), cursor.getDouble(5), cursor.getDouble(6)))
            }
        }
    }
}