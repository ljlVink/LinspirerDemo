package cn.lspdemo_lspfakedata.tools;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;


import org.json.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cn.lspdemo_lspfakedata.linspirer.ICallback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostUtil {
    Context context;

    public PostUtil(Context context){
        this.context=context;

    }

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
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}