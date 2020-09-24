package com.example.uohih.joowon.server;


import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * api
 *
 * date: 2020-09-24
 * author: jieun
 */
public interface ApiService {
//    final String Base_URL = " http://192.168.219.189:3000/process/";
final String Base_URL = "https://joowon12.herokuapp.com/process/";
    @POST("login_p")
    Call<ResponseBody> LoginProcessService(@Body JsonObject jsonBodyString);
    @POST("dairyprocess")
    Call<ResponseBody> EmployeeProcessService(@Body JsonObject jsonBodyString);
    @POST("postprocess")
    Call<ResponseBody> PostProcessService(@Body JsonObject jsonBodyString);
    @POST("boardprocess")
    Call<ResponseBody> BoardProcessService(@Body JsonObject jsonBodyString);

}
