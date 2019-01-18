package com.example.utils;

public interface HttpCallbackListener {
    void finish(String response);
    void onError(Exception e);
}
