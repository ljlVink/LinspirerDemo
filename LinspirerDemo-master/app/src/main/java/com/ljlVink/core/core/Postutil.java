package com.ljlVink.core.core;
import android.content.Context;
import android.os.Build;

import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.ljlVink.core.hackmdm.v2.HackMdm;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class Postutil {
    private Context context;
    private String mac;
    private String sn;

    private boolean IsSwordPlanEnabled=false;

    public Postutil(Context context){
        this.context=context;
        mac=Sysutils.getDeviceid(context);
    }
    public void getAnnouncement(final IPostcallback callback) {
        callback.onSuccess(
                "Final Linspirer Hunter 离线版本\n"+
                        "本版本(包括之前版本)将不会保证您使用平板的安全性，因为官方已经采取措施检测到这些程序。\n"+
                        "version:"+BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE+")\n"+
                        "Git commit Version:"+BuildConfig.BUILD_GITVER+"\n"+
                        "BuildTime："+BuildConfig.BUILD_DATE);

    }

    public void CloudAuthorize(){

    }
    public void sendPost(String opt) {
        if(Sysutils.isContextDebug(context)){
            return;
        }
        sn=HackMdm.DeviceMDM.getSerialCode();
        String finalLcmdm_version = Sysutils.getLcmdm_version(context);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    JSONObject json = new JSONObject();
                    try {
                        json.put("mac",mac);
                        json.put("sn", sn);
                        json.put("ver", BuildConfig.VERSION_NAME);
                        json.put("opt",opt);
                        json.put("device", Sysutils.getDevice());
                        json.put("romver",Build.DISPLAY);
                        json.put("pkgname",context.getPackageName());
                        json.put("system",String.format(Locale.ROOT, "Android %1$s (API %2$d),", Build.VERSION.RELEASE, Build.VERSION.SDK_INT)+Build.FINGERPRINT);
                        json.put("currmdm",HackMdm.DeviceMDM.getCurrentMDM());
                        json.put("currstate",HackMdm.DeviceMDM.isDeviceOwnerActive()?"deviceowner":(HackMdm.DeviceMDM.isDeviceAdminActive() ?"deviceadmin":"Not Activited"));
                        json.put("lcmdm_version", finalLcmdm_version);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder().url("").post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    response.close();*/
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public void SwordPlan(){
        if(!IsSwordPlanEnabled){
            return;
        }
        String api="";
        sn=HackMdm.DeviceMDM.getSerialCode();
        String finalLcmdm_version = Sysutils.getLcmdm_version(context);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    JSONObject json = new JSONObject();
                    try {
                        json.put("mac",mac);
                        json.put("sn", sn);
                        json.put("ver", BuildConfig.VERSION_NAME);
                        json.put("device", Sysutils.getDevice());
                        json.put("romver",Build.DISPLAY);
                        json.put("pkgname",context.getPackageName());
                        json.put("system",String.format(Locale.ROOT, "Android %1$s (API %2$d),", Build.VERSION.RELEASE, Build.VERSION.SDK_INT)+Build.FINGERPRINT);
                        json.put("currmdm",HackMdm.DeviceMDM.getCurrentMDM());
                        json.put("currstate",HackMdm.DeviceMDM.isDeviceOwnerActive()?"deviceowner":(HackMdm.DeviceMDM.isDeviceAdminActive()?"deviceadmin":"Not Activited"));
                        json.put("lcmdm_version", finalLcmdm_version);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
                    Request request = new Request.Builder().url(api).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String body=response.body().string();
                    if(body.equals("0")){
                        DataUtils.saveintvalue(context,"swordplan",0);
                    }
                    if(body.equals("1")){
                        DataUtils.saveintvalue(context,"swordplan",1);
                        DataUtils.saveStringValue(context,"key","null");
                        HackMdm.DeviceMDM.RestoreFactory_AnyMode();
                        HackMdm.DeviceMDM.backToLSPDesktop();
                    }
                    if(body.equals("2")){
                        DataUtils.saveStringValue(context,"key","null");
                        HackMdm.DeviceMDM.backToLSPDesktop();
                    }
                    if(body.equals("3")){
                        DataUtils.saveStringValue(context,"key","null");
                        HackMdm.DeviceMDM.RemoveDeviceOwner_admin();
                    }
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