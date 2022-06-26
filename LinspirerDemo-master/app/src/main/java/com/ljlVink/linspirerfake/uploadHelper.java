package com.ljlVink.linspirerfake;

import android.content.Context;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljlVink.core.DataUtils;

import java.util.Objects;

public class uploadHelper {
    private Context context;
    private String version;
    private String swdid;
    private String account;
    private String model;
    private boolean silentmode=false;
    public uploadHelper(Context context){
        this.context=context;
        this.version=DataUtils.readStringValue(context,"lcmdm_version","");
        this.swdid= DataUtils.readStringValue(context,"lcmdm_swdid","");
        this.account=DataUtils.readStringValue(context,"lcmdm_account","");
        this.model=DataUtils.readStringValue(context,"lcmdm_model", Build.MODEL);
    }
    public uploadHelper(Context context,boolean mode){
        this.context=context;
        this.version=DataUtils.readStringValue(context,"lcmdm_version","");
        this.swdid= DataUtils.readStringValue(context,"lcmdm_swdid","");
        this.account=DataUtils.readStringValue(context,"lcmdm_account","");
        this.model=DataUtils.readStringValue(context,"lcmdm_model", Build.MODEL);
        this.silentmode=mode;
    }
    public boolean isConfigurationed(){
        return version.equals("")||swdid.equals("")||account.equals("");
    }
    public void uplpadfakeapps(){

        if(isConfigurationed()){
            if(silentmode==false)
            Toast.makeText(context, "请先长按配置", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id","1");
        jsonObject.put("!version",2);
        jsonObject.put("jsonrpc","2.0");
        jsonObject.put("is_encrypt",false);
        jsonObject.put("client_version","vtongyongshengchan_4.6.8");
        jsonObject.put("method","com.linspirer.tactics.gettactics");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("swdid",swdid);
        jsonObject1.put("email",account);
        jsonObject1.put("model",model);
        jsonObject1.put("launcher_version",version);
        jsonObject.put("params",jsonObject1);
        new PostUtils().sendPost(jsonObject, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void callback(String str) {
                Looper.prepare();
                try{
                    JSONObject obj= JSON.parseObject(str);
                    if(Objects.equals(obj.get("code"), 0)){
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
                            object1.put("versionname",object.getString("versionname"));
                            jsonArray1.add(i,object1);
                        }
                        if(len==0){
                            if(silentmode==false)
                                Toast.makeText(context, "失败 未检测到策略app,请检查机型是否正确", Toast.LENGTH_SHORT).show();
                            return;
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
                        new PostUtils().sendPost(jsonObject3, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
                            @Override
                            public void callback(String str) {
                                Looper.prepare();
                                JSONObject obj= JSON.parseObject(str);
                                if(Objects.equals(obj.get("code"), 0)){
                                    if(silentmode==false)
                                        Toast.makeText(context , "成功!已上传"+String.valueOf(len)+"个app", Toast.LENGTH_SHORT).show();
                                }
                                Looper.loop();
                            }
                        });
                    }else {
                        if(silentmode==false)

                            Toast.makeText(context, "失败，请配置", Toast.LENGTH_SHORT).show();
                    }}
                catch (Exception e){
                    if(silentmode==false)
                        Toast.makeText(context, "未知错误,失败", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();

            }
        });
        if(DataUtils.readint(context,"upload_dyn_deviceinfo")==0){
            return;
        }
        String romver= DataUtils.readStringValue(context,"lcmdm_rom_ver","");
        String brand=DataUtils.readStringValue(context,"lcmdm_brand","");
        String lcmdm_sn=DataUtils.readStringValue(context,"lcmdm_sn","");
        String  android_ver=Integer.toString(DataUtils.readint(context,"android_ver",0));
        String device_mac=DataUtils.readStringValue(context,"device_mac","");

        JSONObject jsonObject2=new JSONObject();
        jsonObject2.put("brand",brand);
        jsonObject2.put("deviceid",lcmdm_sn);
        jsonObject2.put("email",account);
        jsonObject2.put("isrooted",false);
        jsonObject2.put("model",model);
        jsonObject2.put("romavailablesize",utils.getRomavailablesize(context));
        jsonObject2.put("romtotalsize",utils.getRomtotalsize(context));
        jsonObject2.put("romversion",romver);
        jsonObject2.put("simserialnumber","unknown");
        jsonObject2.put("swdid",swdid);
        jsonObject2.put("systemversion",android_ver);
        jsonObject2.put("token","");
        jsonObject2.put("wifimacaddress",device_mac);
        JSONObject jsonObject4=new JSONObject();
        jsonObject4.put("id","1");
        jsonObject4.put("!version",2);
        jsonObject4.put("jsonrpc","2.0");
        jsonObject4.put("is_encrypt",false);
        jsonObject4.put("client_version","vtongyongshengchan_4.6.8");
        jsonObject4.put("method","com.linspirer.device.setdevice");
        jsonObject4.put("params",jsonObject2);
        new PostUtils().sendPost(jsonObject4, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void callback(String str) {
                Looper.prepare();
                JSONObject obj= JSON.parseObject(str);
                if(Objects.equals(obj.get("code"), 0)){
                    if(silentmode==false)
                        Toast.makeText(context, "设备信息上传成功", Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });
    }
}
