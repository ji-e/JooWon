package com.example.uohih.joowon.repository

import android.content.Context
import android.os.AsyncTask
import com.example.uohih.joowon.base.LogUtil
import com.example.uohih.joowon.database.AsyncCallback
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.io.IOException


open class GoogleIdAsyncTask(context: Context) : AsyncTask<Void, Void, String>() {
    private var mContext = context
    private var googleAsyncTaskCallback: AsyncCallback? = null
    fun setGoogleAsyncCallback(callback: AsyncCallback) {
        this.googleAsyncTaskCallback = callback
    }
    override fun doInBackground(vararg params: Void): String {
        var adId=""

        try {
            adId = AdvertisingIdClient.getAdvertisingIdInfo(mContext).id
            LogUtil.d("adid : $adId")
        } catch (ex: IllegalStateException) {
            LogUtil.d("IllegalStateException$ex")
        } catch (ex: GooglePlayServicesRepairableException) {
            LogUtil.d("GooglePlayServicesRepairableException$ex")
        } catch (ex: IOException) {
            LogUtil.d("IOException$ex")
        } catch (ex: GooglePlayServicesNotAvailableException) {
            LogUtil.d("GooglePlayServicesNotAvailableException$ex")
        } catch (ex: Exception) {
            LogUtil.d("Exception$ex")
        }
        googleAsyncTaskCallback?.doInBackground()
        return adId
    }

    override fun onPostExecute(adId: String) {
        googleAsyncTaskCallback?.onPostExecute(adId)
    }

    fun executeSync() {
        // execute
       execute()

    }
}