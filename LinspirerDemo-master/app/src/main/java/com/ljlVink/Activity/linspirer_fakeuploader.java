package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.core.DataUtils;
import com.ljlVink.linspirerfake.ICallback;
import com.ljlVink.linspirerfake.PostUtils;
import com.ljlVink.linspirerfake.utils;

import java.util.Locale;
import java.util.Objects;

public class linspirer_fakeuploader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linspirer_fakeuploader);
        TextView tv_version=findViewById(R.id.launcher_version);
        TextView tv_swdid=findViewById(R.id.linspirer_swdid);
        TextView tv_account=findViewById(R.id.launcher_email);
        TextView tv_model=findViewById(R.id.device_model);
        TextView tv_rom_ver=findViewById(R.id.rom_ver);
        TextView tv_brand=findViewById(R.id.brand);
        TextView tv_devicesn=findViewById(R.id.device_sn);
        TextView tv_androidver=findViewById(R.id.android_ver);
        Button btn_gettastics=findViewById(R.id.btn_gettastics);
        Button btn_save=findViewById(R.id.btn_save);
        TextView textview_information=findViewById(R.id.textview_information);

        String lcmdm_version="";
        try{
            lcmdm_version=getPackageManager().getPackageInfo("com.android.launcher3",0).versionName;
        }catch (Exception e){
            lcmdm_version="";
        }
        //if(DataUtils.readStringValue(this,"lcmdm_version","").equals("")){
        //    tv_version.setText(lcmdm_version);
        //}
        tv_version.setText(DataUtils.readStringValue(this,"lcmdm_version",lcmdm_version));
        tv_swdid.setText(DataUtils.readStringValue(this,"lcmdm_swdid",""));
        tv_account.setText(DataUtils.readStringValue(this,"lcmdm_account",""));
        tv_model.setText(DataUtils.readStringValue(this,"lcmdm_model", Build.MODEL));
        tv_rom_ver.setText(DataUtils.readStringValue(this,"lcmdm_rom_ver",Build.DISPLAY));
        tv_brand.setText(DataUtils.readStringValue(this,"lcmdm_brand",Build.BRAND));
        tv_devicesn.setText(DataUtils.readStringValue(this,"lcmdm_sn",""));
        tv_androidver.setText(DataUtils.readStringValue(this,"android_ver", utils.getsystemversion()));
        btn_gettastics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String version=tv_version.getText().toString();
                String swdid=tv_swdid.getText().toString();
                String account=tv_account.getText().toString();
                String model=tv_model.getText().toString();
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
                        try{
                        JSONObject obj= JSON.parseObject(str);
                        Log.e("lspd",str);
                        if(Objects.equals(obj.get("code"), 0)){
                            String result="";
                            JSONObject obj1=obj.getJSONObject("data");
                            JSONObject obj2=obj1.getJSONObject("app_tactics");
                            JSONArray jsonArray=obj2.getJSONArray("applist");
                            int len=jsonArray.size();
                            for(int i=0;i<len;i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                result+="app名:"+object.getString("name")+" "+"app包名:"+object.getString("packagename")+" "+"app版本名:"+object.getString("versionname")+" "+"app版本号:"+String.valueOf(object.getInteger("versioncode"))+"\n";
                            }
                            final String result1=result;
                            textview_information.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(result1!="")
                                    textview_information.setText("成功解析!\n"+result1);
                                    else{
                                        textview_information.setText("未检测到策略的app!\n请检查机型是否正确!");
                                    }
                                }});

                        }else {
                            textview_information.post(new Runnable() {
                                @Override
                                public void run() {
                                    textview_information.setText("失败，请检查账号或者swdid是否正常，通常第三方sso登录时账号与sso账号不同\n"+str);
                                }});
                        }}
                        catch (Exception e){
                            textview_information.post(new Runnable() {
                                @Override
                                public void run() {
                                    textview_information.setText("出现未知错误\n"+e.toString());
                                }});
                        }
                    }
                });
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
                String android_ver=tv_androidver.getText().toString();
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_version",version);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_swdid",swdid);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_account",account);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_model",model);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_rom_ver",rom_ver);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_sn",sn);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_brand",brand);
                DataUtils.saveStringValue(getApplicationContext(),"android_ver",android_ver);
                Toast.makeText(linspirer_fakeuploader.this, "已保存", Toast.LENGTH_SHORT).show();
            }
        });

    }
}