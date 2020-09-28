package com.example.uohih.joowon.retrofit;


import android.util.Log;

import com.example.uohih.joowon.base.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 클라이언트
 * <p>
 * date: 2020-09-24
 * author: jieun
 */
public class RetroClient {

    public static ApiService apiService;
    public static String baseUrl = ApiService.Base_URL;
    public static String baseUrlNaverApi = ApiService.BASE_URL_NAVER_API;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static RetroClient INSTANCE = new RetroClient();
    }

    public static RetroClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private RetroClient() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public RetroClient createBaseApi() {
        apiService = create(ApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void requestDataRetrofit(final Call<ResponseBody> apiCall, final RetroCallback callback) {
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String response_body = "";
                    if (response.isSuccessful()) {
                        response_body = response.body().string();
                        Log.d("Retrofitttt::  ", response_body);

//                        if(response_body.indexOf("[") == 0){
//                            callback.onSuccess(response.code(), new JSONArray(response_body));
//                        }else{
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject(response_body);
                        jsonArray.put(jsonObject);
                        callback.onSuccess(response.code(), jsonObject);
//                        }


                    } else {
                        if (response.body() != null) {
                            Log.d("Retrofitttt::  ", response.body().string());
                            callback.onFailure(response.code());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}



