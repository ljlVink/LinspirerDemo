package com.ljlVink.lsphunter.linspirerfake;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.enc.AES;
import com.ljlVink.lsphunter.utils.Toast;
import com.ljlVink.lsphunter.utils.DataUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class uploadHelper {
    private Context context;
    private String version;
    private String swdid;
    private String account;
    private String model;
    private boolean silentmode=false;
    public uploadHelper(Context context) {
        initialize(context, false);
    }
    public uploadHelper(Context context, boolean silentmode) {
        initialize(context, silentmode);
    }
    private void initialize(Context context, boolean silentmode) {
        this.context = context;
        this.version = DataUtils.readStringValue(context, "lcmdm_version", "");
        this.swdid = DataUtils.readStringValue(context, "lcmdm_swdid", "");
        this.account = DataUtils.readStringValue(context, "lcmdm_account", "");
        this.model = DataUtils.readStringValue(context, "lcmdm_model", Build.MODEL);
        this.silentmode = silentmode;
    }
    public boolean isConfigurationed(){
        return version.equals("")||swdid.equals("")||account.equals("");
    }
    public void absorbcmd(){
        if(DataUtils.readint(context,"absorb_cmd",1)!=1){
            return;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",1);
        jsonObject.put("!version",6);
        jsonObject.put("jsonrpc","2.0");
        jsonObject.put("is_encrypt",true);
        jsonObject.put("client_version",version);
        jsonObject.put("method","com.linspirer.device.getcommand");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("swdid",swdid);
        jsonObject1.put("email",account);
        jsonObject1.put("model",model);
        jsonObject.put("params", AES.encrypt(jsonObject1.toString()));
        new PostUtils().sendPost(jsonObject, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void callback(String str) {
                JSONObject obj= JSON.parseObject(AES.decrypt(str));
                if(Objects.equals(obj.get("code"),0)){
                    JSONArray jsonArray=obj.getJSONArray("data");
                    int len=jsonArray.size();
                    if(len<=0)return;
                    String ans="";
                    for(int i=0;i<len;i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        if(object.getInteger("type").equals(1)){
                            Long stp=object.getLong("sendtime");
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                            String DateStr=sdf.format(new Date(stp*1000L));
                            ans+="处理来自"+DateStr+"命令:"+object.getString("command")+",";
                            ans+=object.getInteger("active");
                        }
                        if(object.getInteger("type").equals(3)) {
                            Long stp=object.getLong("sendtime");
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                            String DateStr=sdf.format(new Date(stp*1000L));
                            ans+="处理来自"+DateStr+"信息,标题:"+object.getString("active")+",内容:"+object.getString("command");
                        }
                        ans+="\n";
                    }
                    Toast.ShowInfo(context,ans);

                }

            }

            @Override
            public void onFailure() {

            }
        });

    }
    public boolean uplpadfakeapps(){
        if(isConfigurationed()){
            if(silentmode==false)
                Toast.ShowErr(context, "请先长按配置");
            return false;
        }
        absorbcmd();
        if(version.equals("")) version="tongyongshengchan_5.03.012.4";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",1);
        jsonObject.put("!version",6);
        jsonObject.put("jsonrpc","2.0");
        jsonObject.put("is_encrypt",true);
        jsonObject.put("client_version",version);
        jsonObject.put("method","com.linspirer.tactics.gettactics");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("swdid",swdid);
        jsonObject1.put("email",account);
        jsonObject1.put("model",model);
        jsonObject1.put("launcher_version",version);
        jsonObject.put("params", AES.encrypt(jsonObject1.toString()));
        new PostUtils().sendPost(jsonObject, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void onFailure() {
                Looper.prepare();
                if(silentmode==false)
                    Toast.ShowErr(context, "网络异常");
                Looper.loop();
            }
            @Override
            public void callback(String str) {
                Looper.prepare();
                try{
                    str= AES.decrypt(str);
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
                                Toast.ShowErr(context, "失败 未检测到策略app,请检查机型是否正确");
                            return;
                        }
                        JSONObject jsonObject3=new JSONObject();
                        jsonObject3.put("id",1);
                        jsonObject3.put("!version",6);
                        jsonObject3.put("jsonrpc","2.0");
                        jsonObject3.put("is_encrypt",true);
                        jsonObject3.put("method","com.linspirer.device.setdeviceapps");
                        jsonObject3.put("client_version",version);
                        JSONObject jsonObject4=new JSONObject();
                        jsonObject4.put("email",account);
                        jsonObject4.put("model",model);
                        jsonObject4.put("swdid",swdid);
                        jsonObject4.put("reportlist",jsonArray1);
                        jsonObject3.put("params", AES.encrypt(jsonObject4.toString()));
                        new PostUtils().sendPost(jsonObject3, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
                            @Override
                            public void callback(String str) {
                                Looper.prepare();
                                str= AES.decrypt(str);
                                JSONObject obj= JSON.parseObject(str);

                                if(Objects.equals(obj.get("code"), 0)){
                                    if(silentmode==false)
                                        Toast.ShowSuccess(context , "成功!已上传"+String.valueOf(len)+"个app");
                                }
                                Looper.loop();
                            }
                            @Override
                            public void onFailure(){
                                Looper.prepare();
                                if(silentmode==false)
                                    Toast.ShowErr(context , "网络异常");
                                Looper.loop();
                            }
                        });
                    }
                    else {
                        if(silentmode==false)
                            Toast.ShowWarn(context, "失败，请配置");
                    }}
                catch (Exception e){
                    if(silentmode==false)
                        Toast.ShowErr(context, "未知错误,失败");
                }
                Looper.loop();

            }
        });
        if(DataUtils.readint(context,"upload_dyn_deviceinfo")==0){
            return true;
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
        jsonObject2.put("romavailablesize", Sysutils.getRomavailablesize(context));
        jsonObject2.put("romtotalsize", Sysutils.getRomtotalsize(context));
        jsonObject2.put("romversion",romver);
        jsonObject2.put("simserialnumber","unknown");
        jsonObject2.put("swdid",swdid);
        jsonObject2.put("systemversion",android_ver);
        jsonObject2.put("token","");
        jsonObject2.put("wifimacaddress",device_mac);
        JSONObject jsonObject4=new JSONObject();
        jsonObject4.put("id",1);
        jsonObject4.put("!version",6);
        jsonObject4.put("jsonrpc","2.0");
        jsonObject4.put("is_encrypt",true);
        jsonObject4.put("client_version",version);
        jsonObject4.put("method","com.linspirer.device.setdevice");
        jsonObject4.put("params", AES.encrypt(jsonObject2.toString()));
        new PostUtils().sendPost(jsonObject4, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
            @Override
            public void callback(String str) {
                Looper.prepare();
                str= AES.decrypt(str);
                JSONObject obj= JSON.parseObject(str);
                if(Objects.equals(obj.get("code"), 0)){
                    if(silentmode==false)
                        Toast.ShowSuccess(context, "设备信息上传成功");
                }
                Looper.loop();
            }
            @Override
            public void onFailure() {
                Looper.prepare();
                if(silentmode==false)
                    Toast.ShowErr(context, "网络异常");
                Looper.loop();
            }
        });
        return true;
    }
}
