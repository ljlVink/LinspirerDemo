package com.ljlVink.linspirerfake;

import android.content.Context;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljlVink.core.DataUtils;

import java.util.Objects;

public class uploadHelper {
    private Context context;
    public uploadHelper(Context context){
        this.context=context;
    }
    public void uplpadfakeapps(){
        String version=DataUtils.readStringValue(context,"lcmdm_version","");
        String swdid= DataUtils.readStringValue(context,"lcmdm_swdid","");
        String account=DataUtils.readStringValue(context,"lcmdm_account","");
        String model=DataUtils.readStringValue(context,"lcmdm_model", Build.MODEL);
        if(version.equals("")||swdid.equals("")||account.equals("")){
            Toast.makeText(context, "请先长按配置", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id","1");
        jsonObject.put("!version",2);
        jsonObject.put("jsonrpc","2.0");
        jsonObject.put("is_encrypt",false);
        jsonObject.put("client_version","vtongyongshengchan_4.6.8");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("swdid",swdid);
        jsonObject1.put("email",account);
        jsonObject1.put("model",model);
        jsonObject1.put("launcher_version",version);
        jsonObject.put("method","com.linspirer.tactics.gettactics");
        jsonObject.put("params",jsonObject1);
        new PostUtils().sendPost(jsonObject, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void callback(String str) {
                Looper.prepare();
                try{
                    JSONObject obj= JSON.parseObject(str);
                    Log.e("lspd",str);
                    if(Objects.equals(obj.get("code"), 0)){
                        String result="";
                        JSONObject obj1=obj.getJSONObject("data");
                        JSONObject obj2=obj1.getJSONObject("app_tactics");
                        JSONArray jsonArray=obj2.getJSONArray("applist");
                        int len=jsonArray.size();
                        JSONArray jsonArray1=new JSONArray();
                        for(int i=0;i<len;i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            JSONObject object1=new JSONObject();
                            object1.put("appname",object.getString("name"));
                            object1.put("isWhiteApp",1);
                            object1.put("issystemapp",0);
                            object1.put("packagename",object.getString("packagename"));
                            object1.put("versioncode",object.getInteger("versioncode"));
                            jsonArray1.add(i,object1);
                        }
                        JSONObject jsonObject3=new JSONObject();
                        jsonObject3.put("id","1");
                        jsonObject3.put("!version",2);
                        jsonObject3.put("jsonrpc","2.0");
                        jsonObject3.put("is_encrypt",false);
                        jsonObject3.put("method","com.linspirer.device.setdeviceapps");
                        jsonObject3.put("client_version","vtongyongshengchan_4.6.8");
                        JSONObject jsonObject4=new JSONObject();
                        jsonObject4.put("email",account);
                        jsonObject4.put("model",model);
                        jsonObject4.put("swdid",swdid);
                        jsonObject4.put("reportlist",jsonArray1);
                        jsonObject3.put("params",jsonObject4);
                        Log.e("tag",jsonObject3.toString());
                        new PostUtils().sendPost(jsonObject3, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
                            @Override
                            public void callback(String str) {
                                Looper.prepare();
                                JSONObject obj= JSON.parseObject(str);
                                if(Objects.equals(obj.get("code"), 0)){
                                    Log.e("lspd",str);
                                    Toast.makeText(context , "成功!已上传"+String.valueOf(len)+"个app", Toast.LENGTH_SHORT).show();
                                }
                                Looper.loop();
                            }
                        });
                    }else {
                        Toast.makeText(context, "失败，请配置", Toast.LENGTH_SHORT).show();
                    }}
                catch (Exception e){
                    Toast.makeText(context, "未知错误,失败", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();

            }
        });
/*
        JSONObject jsonObject2=new JSONObject();
        jsonObject2.put("brand",);

        jsonObject2.put("email",account);
        jsonObject2.put("isrooted",false);
        jsonObject2.put("model",);*/
/*
        new PostUtils().sendPost(, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void callback(String str) {

            }
        });*/
    }
}
