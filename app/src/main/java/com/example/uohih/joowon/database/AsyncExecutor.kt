package com.example.uohih.joowon.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import com.example.uohih.joowon.view.CustomLoadingBar


open class AsyncExecutor(mContext: Context) : AsyncTask<String, Void?, Cursor?>() {

    var callback: AsyncCallback? = null
    var db: SQLiteDatabase? = null
    var mContext = mContext

    val asyncDialog by lazy { CustomLoadingBar.createCustomLoadingBarDialog(mContext) }

    fun setAsyncExecutorDB(db: SQLiteDatabase) {
        this.db = db
    }

    fun setAsyncCallback(callback: AsyncCallback) {
        this.callback = callback
    }

    override fun onPostExecute(result: Cursor?) {
        super.onPostExecute(result)

//        asyncDialog.dismiss()

        callback?.onPostExecute()

    }

    override fun doInBackground(vararg params: String?): Cursor? {
        if ("select" == params[0]) {
            val query = params[1].toString()
            return db?.rawQuery(query, null)
        } else {
            callback?.doInBackground()
            return null
        }


    }
}