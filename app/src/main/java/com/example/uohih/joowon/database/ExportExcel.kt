//package com.example.uohih.dailylog.database
//
//import android.content.Context
//import android.widget.Toast
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//
///**
// * 엑셀파일 만들기
// * mContext: Context
// */
//class ExportExcel(mContext: Context) {
//
//    private val mContext = mContext
//    // 파일명
//    private val fileName = "dLog.xlsx"
//    // 파일 위치
//    private val fileDir = "/mnt/sdcard/Download"
//    // 파일 총 명
//    private val totalName = "$fileDir/$fileName"
//
//    private val db = DBHelper(mContext)
//    private var dataList = ArrayList<DBData>()
//    private var columns = arrayOf("NO", "DATE", "TITLE", "CONTENT")
//    private val workbook: XSSFWorkbook = XSSFWorkbook()
//    private val sheet = workbook.createSheet("dlog_sheet")
//    private val createHelper = workbook.creationHelper
//
//
//    /**
//     * 엑셀 파일 존재 여부 확인
//     * fileName: String: 파일명, type: Boolean: 퍼미션 체크 확인?
//     * return Boolean
//     * true: 있음, false: 없음
//     */
//    fun fileExists(fileName: String, type: Boolean): Boolean {
//        val fileDir = "/mnt/sdcard/Download"
//        val totalName = "$fileDir/$fileName"
//
//        val file = File(totalName)
//        if (file.exists() && !type) {
//            file.delete()
//            return true
//        } else if (file.exists() && type) {
//            return true
//        }
//        return false
//    }
//
//
//    /**
//     * 엑셀파일 만들기
//     */
//    fun makeExcelFile() {
//        // 파일 존재 확인
//        fileExists(fileName, false)
//
//        val headerFont = workbook.createFont()
//        headerFont.bold = true
//        headerFont.boldweight = (Font.BOLDWEIGHT_BOLD)
//        headerFont.color = IndexedColors.BLUE_GREY.getIndex()
//
//        val headerCellStyle = workbook.createCellStyle()
//        headerCellStyle.setFont(headerFont)
//
//        val headerRow = sheet.createRow(0)
//
//
//        for (i in 0 until columns.size) {
//            var cell = headerRow.createCell(i)
//            cell.setCellValue(columns[i])
//            cell.cellStyle = headerCellStyle
//        }
//
//        // 엑셀시트에 데이터 삽입
//        insertSheetData()
//    }
//
//    /**
//     * 엑셀시트에 데이터 삽입
//     */
//    fun insertSheetData() {
//        // DB 데이터 List로 가져오기
//        SQLiteToList()
//
//        val dateCellStyle = workbook.createCellStyle()
//        dateCellStyle.dataFormat = createHelper.createDataFormat().getFormat("yyyy-mm-dd")
//
//        var rowNum = 1
//
//        for (data in dataList) {
//            val row = sheet.createRow(rowNum++)
//            // no
//            row.createCell(0).setCellValue(data.no.toString())
//            // date
//            val dateCell = row.createCell(1)
//            dateCell.setCellValue(data.date.toString())
//            dateCell.cellStyle = dateCellStyle
//            //title
//            row.createCell(2).setCellValue(data.title)
//            //content
//            row.createCell(3).setCellValue(data.content)
//
//        }
//
//        sheet.setColumnWidth(1, 3000)
//        sheet.setColumnWidth(2, 5000)
////        for (i in 0 until columns.size) {
////            sheet.autoSizeColumn(i)
////        }
//        writeExcelFile()
//    }
//
//    /**
//     * 엑셀 파일 쓰기
//     */
//    fun writeExcelFile() {
//        val fileout = FileOutputStream(totalName)
//        workbook.write(fileout)
//        fileout.close()
//        Toast.makeText(mContext, "엑셀로 내보내기가 완료되었습니다.", Toast.LENGTH_SHORT).show()
//    }
//
//    /**
//     * 엑셀 파일 읽기
//     */
//    fun readExcelFile() {
//        if (!fileExists(fileName, true)) {
//            return
//        }
//
//        val excelFile = FileInputStream(File(totalName))
//        val workbook = XSSFWorkbook(excelFile)
//        val sheet = workbook.getSheet("dlog_sheet")
//
//        for (rowIndex in 1 until sheet.physicalNumberOfRows) {
//            val row = sheet.getRow(rowIndex)
//            if (row != null) {
//                var no: String = ""
//                var date: String = ""
//                var title: String = ""
//                var content: String = ""
//                for (columnIndex in 0 until row.physicalNumberOfCells) {
//                    val cell = row.getCell(columnIndex)
//                    if (cell != null) {
//                        when (columnIndex) {
//                            0 -> no = cell.stringCellValue
//                            1 -> date = cell.stringCellValue
//                            2 -> title = cell.stringCellValue
//                            3 -> content = cell.stringCellValue
//                        }
//                    }
//                }
//                dataList.add(DBData(no.toInt(), date.toInt(), title, content))
//                LogUtil.d(date)
//            }
//        }
//        excelFile.close()
//
//        db.onReset()
//
//        for (i in 0 until dataList.size) {
//            db.insert(dataList[i].date, dataList[i].title, dataList[i].content)
//        }
//        Toast.makeText(mContext, "엑셀 가져오기가 완료되었습니다.", Toast.LENGTH_SHORT).show()
//    }
//
//    /**
//     * SQLite DB 데이터 리스트로 가져오기
//     */
//    fun SQLiteToList() {
//        var cursor = db.selectAll()
//
//        dataList.clear()
//        while (cursor.moveToNext()) {
//            if (cursor.getString(1) != null) {
//                LogUtil.d(cursor.getString(0) + "  :   " + cursor.getString(1) + "  :   " + cursor.getString(2) + "  :   " + cursor.getString(3))
//                dataList.add(DBData(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3)))
//            }
//        }
//    }
//}