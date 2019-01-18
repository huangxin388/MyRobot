package com.example.utils;

import android.util.Log;

import com.example.bean.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private static final String URL = "http://openapi.tuling123.com/openapi/api/v2";
    private static final String API_KEY = "7bdfd1b20f084b8089eeaf289799c68d";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    /**
     * 使用OkHttp自身回调请求数据
     * @param msg
     * @param callback
     * enqueue()方法中会自动开启线程
     */
    public static void sendOkHttpRequest(String msg, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        final String json = "{" +
                "\"reqType\":0," +
                "    \"perception\": {" +
                "        \"inputText\": {" +
                "            \"text\": \"" + msg + "\"" +
                "        }," +
                "        \"inputImage\": {" +
                "            \"url\": \"\"" +
                "        }," +
                "        \"selfInfo\": {" +
                "            \"location\": {" +
                "                \"city\": \"天津\"," +
                "                \"province\": \"天津\",\n" +
                "                \"street\": \"天津理工大学\"\n" +
                "            }" +
                "        }" +
                "    }," +
                "    \"userInfo\": {" +
                "        \"apiKey\": \"" + API_KEY + "\"," +
                "        \"userId\": \"" + "572780350" + "\"" +
                "    }" +
                "}";
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        //注意这里用的是enqueue()方法，此方法会在内部自动开启一个线程
        client.newCall(request).enqueue(callback);
    }


    /**
     * 自己构造回调方法请求数据，测试时使用
     * @param msg
     * @param listener
     * 因为是网络请求，因此需要开启线程
     */
    public static void doRequest(String msg, final HttpCallbackListener listener) {
        final String json = "{" +
                "\"reqType\":0," +
                "    \"perception\": {" +
                "        \"inputText\": {" +
                "            \"text\": \"" + msg + "\"" +
                "        }," +
                "        \"inputImage\": {" +
                "            \"url\": \"\"" +
                "        }," +
                "        \"selfInfo\": {" +
                "            \"location\": {" +
                "                \"city\": \"天津\"," +
                "                \"province\": \"天津\",\n" +
                "                \"street\": \"天津理工大学\"\n" +
                "            }" +
                "        }" +
                "    }," +
                "    \"userInfo\": {" +
                "        \"apiKey\": \"" + API_KEY + "\"," +
                "        \"userId\": \"" + "572780350" + "\"" +
                "    }" +
                "}";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String strResult = "";
                Response responseData = null;
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON,json);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .build();
                try {
                    responseData = client.newCall(request).execute();
                    String response = responseData.body().string();
                    Log.d("xxx","请求成功" + responseData);
                    strResult = parJson(response);
                    if(listener != null) {
                        listener.finish(strResult);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if(listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    responseData.body().close();
                }
            }
        }).start();
    }

    private static String parJson(String responseData) {
        Gson gson = new Gson();
        String strResult = "";
        Result result = gson.fromJson(responseData,new TypeToken<Result>(){}.getType());
        strResult = result.getResults().get(0).getValues().getText();
        Log.d("xxx","返回的结果为：" + strResult);
        return strResult;
    }


//    {
//        "emotion":
//        {
//            "robotEmotion":
//            {
//                "a":0,"d":0,"emotionId":0,"p":0
//            },
//            "userEmotion":
//            {
//                "a":0,"d":0,"emotionId":10300,"p":0
//            }
//        },
//        "intent":
//        {
//            "actionName":"",
//                "code":10004,
//                "intentName":""
//        },
//        "results":
//        [
//                {
//                    "groupType":1,
//                        "resultType":"text",
//                        "values":
//                    {
//                        "text":"你陪我玩我就好啦"
//                    }
//                }
//        ]
//    }
}
