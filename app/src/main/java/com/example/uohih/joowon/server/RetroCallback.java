package com.example.uohih.joowon.server;


import org.json.JSONArray;

/**
 * RetroCallback
 *
 * date: 2020-09-24
 * author: jieun
 */
public interface RetroCallback<T> {
    void onError(Throwable t);

    void onSuccess(int code, JSONArray receivedData);

    void onFailure(int code);
}

