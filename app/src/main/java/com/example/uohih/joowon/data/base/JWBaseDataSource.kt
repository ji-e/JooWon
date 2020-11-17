package com.example.uohih.joowon.data.base

import com.example.uohih.joowon.BuildConfig
import com.example.uohih.joowon.api.ApiService
import com.example.uohih.joowon.base.JWBaseApplication
import com.example.uohih.joowon.retrofit.GetResbodyCallback
import com.google.gson.JsonObject
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class JWBaseDataSource {
    private fun setJsonObject(jsonObject: JsonObject): JsonObject {
        jsonObject.addProperty("timestamp", System.currentTimeMillis())
        jsonObject.addProperty("txid", UUID.randomUUID().toString().replace("-", ""))

        return jsonObject
    }
}