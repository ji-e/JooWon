package com.example.uohih.dailylog.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.uohih.joowon.base.LogUtil

/**
 * DB
 * mContext: Context
 */
class DBHelper(mContext: Context) : SQLiteOpenHelper(mContext, "joowon", null, 1) {

    /**
     * 칼럼
     * no: primary key to autoincrement
     * name: 이름
     * joinDate: 입사날짜
     * phone: 핸드폰 번호
     * use: 사용 휴가 갯수
     * total: 전체 휴가 갯수
     * picture: 사진
     */
    val tableNameWorkerJW = "tb_worker_jw"
    val tableNameVacationJW = "tb_vacation_jw"

    override fun onCreate(db: SQLiteDatabase) {
        val queryCreate = "create table $tableNameWorkerJW (no integer primary key autoincrement, name, joinDate integer, phone, use integer, total integer, picture)"
        db.execSQL(queryCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val queryDrop = "drop table $tableNameWorkerJW"
        db.execSQL(queryDrop)
    }


    /**
     * tb_joowon 초기화
     */
    fun onReset() {
        val db = writableDatabase
        val queryDrop = "drop table $tableNameWorkerJW"
        db.execSQL(queryDrop)
        val queryCreate = "create table $tableNameWorkerJW (no integer primary key autoincrement, name, joinDate integer, phone, use integer, total integer, picture )"
        db.execSQL(queryCreate)
        db.close()
    }


    /**
     * 휴가테이블 생성
     *
     * no: Integer: primary key: autoincrement
     * name: String: 직원이름
     * phone: String: 핸드폰번호
     * date: Integer: 휴가 날짜
     * content: String: 휴가 내용
     * use: Integer: 사용한 휴가 개수
     * total: Integer: 총 휴가 개수
     */
    fun createVacationTable() {
        val db = writableDatabase
        val queryCreate = "create table $tableNameVacationJW (no integer primary key autoincrement, name, phone, date integer, content, use integer, total integer)"
        LogUtil.d(queryCreate)
        db.execSQL(queryCreate)
        db.close()
    }

    /**
     * 데이터 삽입
     */
    fun insert(tableName: String, vararg str: String) {
        val db = writableDatabase

        val sb = StringBuilder()
        sb.append("insert into $tableName (")

        for (i in 0 until str.size / 2) {
            if (i + 1 == str.size / 2) sb.append(str[i] + ") values (")
            else sb.append(str[i] + ", ")
        }

        for (i in str.size / 2 until str.size) {
            if (i + 1 == str.size) sb.append("\'" + str[i] + "\')")
            else sb.append("\'" + str[i] + "\'" + ", ")
        }

        val queryInsert = sb.toString()

        db.execSQL(queryInsert)
        db.close()
        LogUtil.d(queryInsert)
    }

    /**
     * 데이터 수정
     */
    fun update(tableName: String, vararg str: String) {
        val db = writableDatabase
//        val queryUpdate = "update $tableName set title =\'$title\', content=\'$content\' where no=\'$no\'"

        val sb = StringBuilder()
        sb.append("update \'$tableName\' set ")
        sb.append("name =\'${str[0]}\', ")
        sb.append("joinDate =\'${str[1]}\', ")
        sb.append("phone =\'${str[2]}\', ")
        sb.append("use =\'${str[3]}\', ")
        sb.append("total =\'${str[4]}\', ")
        sb.append("picture =\'${str[5]}\' ")
        sb.append("where no=\'${str[6]}\'")

        val queryUpdate = sb.toString()

        db.execSQL(queryUpdate)
        db.close()
        LogUtil.d(queryUpdate)
    }
//
//    /**
//     * 데이터 삭제
//     */
//    fun delete(array: ArrayList<String>, index: String) {
//        val db = writableDatabase
//        for (i in 0 until array.size) {
//            val no = array[i]
////            var queryDelete = "delete from $tableName where $index=\'$no\'"
////            if(index.equals("no")){
//            var queryDelete = "delete from $tableName where $index=$no"
////            }
//            db.execSQL(queryDelete)
//            LogUtil.d(queryDelete)
//        }
//        db.close()
//    }
//
    /**
     * 데이터 검색
     */
    fun selectWorker(name:String, phone:String): Cursor {
        val db = writableDatabase
        val querySelect = "select * from $tableNameWorkerJW where name=\'$name\' and phone = \'$phone\'"
        db.rawQuery(querySelect, null)
        LogUtil.d(querySelect)
        return db.rawQuery(querySelect, null)
    }

    fun selectWorker(no:String): Cursor {
        val db = writableDatabase
        val querySelect = "select * from $tableNameWorkerJW where no=\'$no\'"
        db.rawQuery(querySelect, null)
        LogUtil.d(querySelect)
        return db.rawQuery(querySelect, null)
    }
//
//    fun select(first: Int, last: Int): Cursor {
//        val db = writableDatabase
//        val querySelect = "select * from $tableName where date>=\'$first\' and date<=\'$last\' order by date asc"
//        db.rawQuery(querySelect, null)
//        LogUtil.d(querySelect)
//        return db.rawQuery(querySelect, null)
//    }
//
//    fun select(first: Int, last: Int, str:String): Cursor {
//        val db = writableDatabase
//        val querySelect = "select * from $tableName where date>=\'$first\' and date<=\'$last\' and title like \'%$str%\' order by date asc "
//        db.rawQuery(querySelect, null)
//        LogUtil.d(querySelect)
//        return db.rawQuery(querySelect, null)
//    }
//
//    fun select(str:String): Cursor {
//        val db = writableDatabase
//        val querySelect = "select * from $tableName where title like \'%$str%\' order by date desc"
//        db.rawQuery(querySelect, null)
//        LogUtil.d(querySelect)
//        return db.rawQuery(querySelect, null)
//    }

    fun selectAll(tableName: String): Cursor {
        val db = writableDatabase
        val querySelect = "select * from $tableName"
        db.rawQuery(querySelect, null)
        LogUtil.d(querySelect)
        return db.rawQuery(querySelect, null)
    }

    /**
     * 테이블 존재 확인
     */
    fun getTableExiste(tableName: String): Cursor {
        val db = writableDatabase
        val querySelect = "select count(*) from sqlite_master Where Name = \'$tableName\'"
        db.rawQuery(querySelect, null)
        LogUtil.d(querySelect)
        return db.rawQuery(querySelect, null)
    }

//    fun selectResent(): Cursor {
//        val db = writableDatabase
//        val querySelect = "select * from $tableName order by date desc limit 10"
//        db.rawQuery(querySelect, null)
//        LogUtil.d(querySelect)
//        return db.rawQuery(querySelect, null)
//    }


}