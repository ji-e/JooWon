package com.example.uohih.joowon.retrofit;


import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * api
 *
 * date: 2020-09-24
 * author: jieun
 */
public interface ApiService {
//    final String Base_URL = " http://192.168.219.189:3000/process/";
    String Base_URL = "https://joowon12.herokuapp.com/process/";
    String BASE_URL_NAVER_API = "https://openapi.naver.com/";

    @POST("login_p/{method}")
    Call<ResponseBody> LoginProcessService(@Path("method")  String method,  @Body JsonObject jsonBodyString);
    @POST("base")
    Call<ResponseBody> BaseProcessService(@Body JsonObject jsonBodyString);
    @POST("dairyprocess")
    Call<ResponseBody> EmployeeProcessService(@Body JsonObject jsonBodyString);
    @POST("postprocess")
    Call<ResponseBody> PostProcessService(@Body JsonObject jsonBodyString);
    @POST("boardprocess")
    Call<ResponseBody> BoardProcessService(@Body JsonObject jsonBodyString);

    @POST("v1/nid/me")
    Call<ResponseBody> NaverOpenApiService(@Header("Authorization") String accT);

}
