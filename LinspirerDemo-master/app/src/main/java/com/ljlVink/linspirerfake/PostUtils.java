package com.ljlVink.linspirerfake;


import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostUtils {
    public PostUtils(){}
    public void sendPost(JSONObject jsonObject, String url, final ICallback callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(jsonObject));
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    callback.callback(response.body().string());
                    response.close();
                }
                catch (Exception e){
                    callback.onFailure();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
