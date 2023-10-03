package com.ljlVink.lsphunter.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.lsphunter.linspirerfake.ICallback;
import com.ljlVink.lsphunter.linspirerfake.PostUtils;
import com.ljlVink.lsphunter.utils.DataUtils;
import com.ljlVink.lsphunter.utils.Sysutils;
import com.ljlVink.lsphunter.utils.Toast;
import com.ljlVink.lsphunter.utils.enc.AES;

import java.util.Locale;
import java.util.Objects;

public class FakeUploaderFragment {
    View view;
    Context ctx;
    public FakeUploaderFragment(View view,Context ctx){
        this.view=view;
        this.ctx=ctx;
    }
    public void HandleFragment(){

        TextView tv_version=view.getRootView().findViewById(R.id.launcher_version);
        TextView tv_swdid=view.getRootView().findViewById(R.id.linspirer_swdid);
        TextView tv_account=view.getRootView().findViewById(R.id.launcher_email);
        TextView tv_model=view.getRootView().findViewById(R.id.device_model);
        TextView tv_rom_ver=view.getRootView().findViewById(R.id.rom_ver);
        TextView tv_brand=view.getRootView().findViewById(R.id.brand);
        TextView tv_devicesn=view.getRootView().findViewById(R.id.device_sn);
        TextView tv_androidver=view.getRootView().findViewById(R.id.android_ver);
        TextView textview_information=view.getRootView().findViewById(R.id.textview_information);
        TextView textview_information2=view.getRootView().findViewById(R.id.textview_information2);
        TextView tv_macaddr=view.getRootView().findViewById(R.id.device_mac);
        Button btn_gettastics=view.getRootView().findViewById(R.id.btn_gettastics);
        Button btn_save=view.getRootView().findViewById(R.id.btn_save);
        Button btn_getinfo=view.getRootView().findViewById(R.id.btn_getinfo);
        CheckBox cb=view.getRootView().findViewById(R.id.checkBox_upload_device);
        CheckBox cb2=view.getRootView().findViewById(R.id.checkBox_absorb_command);
        String lcmdm_version="";
        try{
            lcmdm_version=ctx.getPackageManager().getPackageInfo("com.android.launcher3",0).versionName;
        }catch (Exception e){
            lcmdm_version="";
        }
        if(DataUtils.readint(ctx,"upload_dyn_deviceinfo")==0){
            tv_rom_ver.setEnabled(false);
            tv_macaddr.setEnabled(false);
            tv_brand.setEnabled(false);
            tv_androidver.setEnabled(false);
            tv_devicesn.setEnabled(false);
            cb.setChecked(false);
        }else{
            cb.setChecked(true);
            tv_rom_ver.setEnabled(true);
            tv_brand.setEnabled(true);
            tv_androidver.setEnabled(true);
            tv_macaddr.setEnabled(true);
            tv_devicesn.setEnabled(true);
        }
        if(DataUtils.readint(ctx,"absorb_cmd",1)==1){
            cb2.setChecked(true);
        }
        tv_version.setText(DataUtils.readStringValue(ctx,"lcmdm_version",lcmdm_version));
        tv_swdid.setText(DataUtils.readStringValue(ctx,"lcmdm_swdid",""));
        tv_account.setText(DataUtils.readStringValue(ctx,"lcmdm_account",""));
        tv_model.setText(DataUtils.readStringValue(ctx,"lcmdm_model", Build.MODEL));
        tv_rom_ver.setText(DataUtils.readStringValue(ctx,"lcmdm_rom_ver",Build.DISPLAY));
        tv_brand.setText(DataUtils.readStringValue(ctx,"lcmdm_brand",Build.BRAND));
        tv_devicesn.setText(DataUtils.readStringValue(ctx,"lcmdm_sn",""));
        tv_androidver.setText((DataUtils.readStringValue(ctx,"android_ver", Sysutils.getsystemversion_str())));
        tv_macaddr.setText(DataUtils.readStringValue(ctx,"device_mac", Sysutils.getMacaddress(ctx)));
        btn_gettastics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String version=tv_version.getText().toString();
                String swdid=tv_swdid.getText().toString();
                String account=tv_account.getText().toString();
                String model=tv_model.getText().toString();
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id","1");
                jsonObject.put("!version",6);
                jsonObject.put("jsonrpc","2.0");
                jsonObject.put("is_encrypt",false);
                if(version.equals("")){
                    version="tongyongshengchan_5.03.012.0";
                }
                jsonObject.put("client_version",version);
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("swdid",swdid);
                jsonObject1.put("email",account);
                jsonObject1.put("model",model);
                jsonObject1.put("launcher_version",version);
                jsonObject.put("method","com.linspirer.tactics.gettactics");
                jsonObject.put("params", AES.encrypt(jsonObject1.toString()));
                new PostUtils().sendPost(jsonObject, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
                    @Override
                    public void callback(String str) {
                        try{
                            JSONObject obj= JSON.parseObject(AES.decrypt(str));
                            if(Objects.equals(obj.get("code"), 0)){
                                String result="";
                                JSONObject obj1=obj.getJSONObject("data");
                                JSONObject obj2=obj1.getJSONObject("app_tactics");
                                JSONArray jsonArray=obj2.getJSONArray("applist");
                                int len=jsonArray.size();
                                for(int i=0;i<len;i++){
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    result+="app名:"+object.getString("name")+" "+"app包名:"+object.getString("packagename")+" "+"app版本名:"+object.getString("versionname")+" "+"app版本号:"+String.valueOf(object.getInteger("versioncode"))+"应用商店id:"+object.getInteger("id").toString()+"\n";
                                    String sha1=object.getString("sha1");
                                    if (sha1!=null){
                                        result+="sha1:"+sha1+"\n";
                                    }else{
                                        result+="sha1:没有sha1签名验证，此包名可以被改包安装\n";
                                    }
                                    result+="应用商店下载链接:https://cloud.linspirer.com:883/download.php?email="+account+"&appid="+object.getInteger("id").toString()+"&swdid="+swdid+"\n";
                                }
                                final String result1=result;
                                textview_information.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(result1!=""){
                                            textview_information.setText("成功解析!\n"+result1);
                                        }
                                        else{
                                            textview_information.setText("未检测到策略的app!\n此类问题一般为机型设置有误,请检查机型是否正确!");
                                        }
                                        scrolldown();
                                    }});

                            }else {
                                textview_information.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textview_information.setText("失败，请检查账号或者swdid是否正常，通常第三方sso登录时账号与sso账号不同，或者输入设置里的设备mac地址\n"+ AES.decrypt(str));
                                        scrolldown();
                                    }});
                            }}
                        catch (Exception e){
                            textview_information.post(new Runnable() {
                                @Override
                                public void run() {
                                    textview_information.setText("出现未知错误\n"+e.toString());
                                    scrolldown();
                                }});
                        }
                    }
                    @Override
                    public void onFailure(){
                        textview_information.post(new Runnable() {
                            @Override
                            public void run() {
                                textview_information.setText("网路异常\n");
                                scrolldown();
                            }});

                    }
                });

                JSONObject Stuinfo=new JSONObject();
                Stuinfo.put("id","1");
                Stuinfo.put("!version",6);
                Stuinfo.put("jsonrpc","2.0");
                Stuinfo.put("is_encrypt",false);
                Stuinfo.put("client_version","tongyongshengchan_5.03.012.4");
                JSONObject Stuinfo_para=new JSONObject();
                Stuinfo_para.put("swdid",swdid);
                Stuinfo_para.put("model",model);
                Stuinfo_para.put("email",account);
                Stuinfo.put("params",Stuinfo_para);
                Stuinfo.put("method","com.linspirer.user.getuserinfo");
                new PostUtils().sendPost(Stuinfo, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
                    @Override
                    public void callback(String str) {
                        JSONObject obj= JSON.parseObject(AES.decrypt(str));
                        if (Objects.equals(obj.get("code"), 0)){
                            String result="";
                            JSONObject obj1=obj.getJSONObject("data");
                            result+="姓名:" +obj1.getString("name");
                            result+=",学校名:"+obj1.get("school_name");
                            result+=",班级名:"+obj1.get("class_name");
                            result+=",用户userid(用于计算动态码):"+obj1.get("id");
                            final String result1=result;
                            textview_information2.post(new Runnable() {
                                @Override
                                public void run() {
                                    textview_information2.setText(result1);
                                    scrolldown();
                                }
                            });
                        }else{
                            textview_information2.post(new Runnable() {
                                @Override
                                public void run() {
                                    textview_information.setText("获取学生信息失败\n"+ AES.decrypt(str));
                                    scrolldown();
                                }});

                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });

            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()){
                    DataUtils.saveintvalue(ctx,"upload_dyn_deviceinfo",1);
                    tv_rom_ver.setEnabled(true);
                    tv_brand.setEnabled(true);
                    tv_androidver.setEnabled(true);
                    tv_macaddr.setEnabled(true);
                    tv_devicesn.setEnabled(true);
                }
                else{
                    DataUtils.saveintvalue(ctx,"upload_dyn_deviceinfo",0);
                    tv_rom_ver.setEnabled(false);
                    tv_macaddr.setEnabled(false);
                    tv_brand.setEnabled(false);
                    tv_androidver.setEnabled(false);
                    tv_devicesn.setEnabled(false);
                }
            }
        });
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb2.isChecked()){
                    DataUtils.saveintvalue(ctx,"absorb_cmd",1);
                }else DataUtils.saveintvalue(ctx,"absorb_cmd",0);

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String version=tv_version.getText().toString();
                String swdid=tv_swdid.getText().toString().toLowerCase(Locale.ROOT);
                String account=tv_account.getText().toString();
                String model=tv_model.getText().toString();
                String rom_ver=tv_rom_ver.getText().toString();
                String sn=tv_devicesn.getText().toString().toUpperCase(Locale.ROOT);
                String brand=tv_brand.getText().toString();
                String device_mac=tv_macaddr.getText().toString();
                String android_ver=(tv_androidver.getText().toString());
                DataUtils.saveStringValue(ctx,"lcmdm_version",version);
                DataUtils.saveStringValue(ctx,"lcmdm_swdid",swdid.toLowerCase(Locale.ROOT));
                DataUtils.saveStringValue(ctx,"lcmdm_account",account);
                DataUtils.saveStringValue(ctx,"lcmdm_model",model);
                DataUtils.saveStringValue(ctx,"lcmdm_rom_ver",rom_ver);
                DataUtils.saveStringValue(ctx,"lcmdm_sn",sn.toUpperCase(Locale.ROOT));
                DataUtils.saveStringValue(ctx,"lcmdm_brand",brand);
                DataUtils.saveStringValue(ctx,"android_ver",android_ver);
                if(android_ver.contains("10")||android_ver.contains("11")||android_ver.contains("12")||android_ver.contains("13")){
                    DataUtils.saveStringValue(ctx,"device_mac",sn.toUpperCase(Locale.ROOT));
                }
                else DataUtils.saveStringValue(ctx,"device_mac",device_mac.toUpperCase(Locale.ROOT));
                Toast.ShowSuccess(ctx, "已保存");
            }
        });
        btn_getinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent("com.linspirer.edu.obtain.userinfo");
                intent.setPackage("com.android.launcher3");
                intent.putExtra("packageName",ctx.getPackageName());
                ctx.sendBroadcast(intent);
            }
        });
    }
    private void scrolldown(){
        ScrollView sv=view.getRootView().findViewById(R.id.scrollview1);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });    }

}
