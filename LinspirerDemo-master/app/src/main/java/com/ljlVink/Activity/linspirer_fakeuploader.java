package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.ToastUtils.Toast;
import com.ljlVink.core.DataUtils;
import com.ljlVink.linspirerfake.AesUtil;
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
        TextView textview_information=findViewById(R.id.textview_information);
        TextView tv_macaddr=findViewById(R.id.device_mac);
        Button btn_gettastics=findViewById(R.id.btn_gettastics);
        Button btn_save=findViewById(R.id.btn_save);
        CheckBox cb=findViewById(R.id.checkBox_upload_device);
        CheckBox cb2=findViewById(R.id.checkBox_absorb_command);
        String lcmdm_version="";
        try{
            lcmdm_version=getPackageManager().getPackageInfo("com.android.launcher3",0).versionName;
        }catch (Exception e){
            lcmdm_version="";
        }
        if(DataUtils.readint(this,"upload_dyn_deviceinfo")==0){
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
        if(DataUtils.readint(this,"absorb_cmd",1)==1){
            cb2.setChecked(true);
        }
        tv_version.setText(DataUtils.readStringValue(this,"lcmdm_version",lcmdm_version));
        tv_swdid.setText(DataUtils.readStringValue(this,"lcmdm_swdid",""));
        tv_account.setText(DataUtils.readStringValue(this,"lcmdm_account",""));
        tv_model.setText(DataUtils.readStringValue(this,"lcmdm_model", Build.MODEL));
        tv_rom_ver.setText(DataUtils.readStringValue(this,"lcmdm_rom_ver",Build.DISPLAY));
        tv_brand.setText(DataUtils.readStringValue(this,"lcmdm_brand",Build.BRAND));
        tv_devicesn.setText(DataUtils.readStringValue(this,"lcmdm_sn",""));
        tv_androidver.setText((DataUtils.readStringValue(this,"android_ver",utils.getsystemversion_str())));
        tv_macaddr.setText(DataUtils.readStringValue(this,"device_mac",utils.getMacaddress(this)));
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
                jsonObject.put("params", AesUtil.encrypt(jsonObject1.toString()));
                new PostUtils().sendPost(jsonObject, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
                    @Override
                    public void callback(String str) {
                        try{
                        JSONObject obj= JSON.parseObject(AesUtil.decrypt(str));
                        if(Objects.equals(obj.get("code"), 0)){
                            String result="";
                            JSONObject obj1=obj.getJSONObject("data");
                            JSONObject obj2=obj1.getJSONObject("app_tactics");
                            JSONArray jsonArray=obj2.getJSONArray("applist");
                            int len=jsonArray.size();
                            for(int i=0;i<len;i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                result+="app???:"+object.getString("name")+" "+"app??????:"+object.getString("packagename")+" "+"app?????????:"+object.getString("versionname")+" "+"app?????????:"+String.valueOf(object.getInteger("versioncode"))+"\n";
                            }
                            final String result1=result;
                            textview_information.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(result1!=""){
                                        textview_information.setText("????????????!\n"+result1);
                                    }
                                    else{
                                        textview_information.setText("?????????????????????app!\n???????????????????????????????????????,???????????????????????????!");
                                    }
                                    scrolldown();
                                }});

                        }else {
                            textview_information.post(new Runnable() {
                                @Override
                                public void run() {
                                    textview_information.setText("??????????????????????????????swdid??????????????????????????????sso??????????????????sso????????????\n"+AesUtil.decrypt(str));
                                    scrolldown();
                                }});
                        }}
                        catch (Exception e){
                            textview_information.post(new Runnable() {
                                @Override
                                public void run() {
                                    textview_information.setText("??????????????????\n"+e.toString());
                                    scrolldown();
                                }});
                        }
                    }
                    @Override
                    public void onFailure(){
                        textview_information.post(new Runnable() {
                            @Override
                            public void run() {
                                textview_information.setText("????????????\n");
                                scrolldown();
                            }});

                    }
                });
            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()){
                    DataUtils.saveintvalue(getApplicationContext(),"upload_dyn_deviceinfo",1);
                    tv_rom_ver.setEnabled(true);
                    tv_brand.setEnabled(true);
                    tv_androidver.setEnabled(true);
                    tv_macaddr.setEnabled(true);
                    tv_devicesn.setEnabled(true);
                }
                else{
                    DataUtils.saveintvalue(getApplicationContext(),"upload_dyn_deviceinfo",0);
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
                    DataUtils.saveintvalue(getApplicationContext(),"absorb_cmd",1);
                }else DataUtils.saveintvalue(getApplicationContext(),"absorb_cmd",0);

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
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_version",version);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_swdid",swdid.toLowerCase(Locale.ROOT));
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_account",account);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_model",model);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_rom_ver",rom_ver);
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_sn",sn.toUpperCase(Locale.ROOT));
                DataUtils.saveStringValue(getApplicationContext(),"lcmdm_brand",brand);
                DataUtils.saveStringValue(getApplicationContext(),"android_ver",android_ver);
                if(android_ver.contains("10")||android_ver.contains("11")||android_ver.contains("12")||android_ver.contains("13")){
                    DataUtils.saveStringValue(getApplicationContext(),"device_mac",sn.toUpperCase(Locale.ROOT));
                }
                else DataUtils.saveStringValue(getApplicationContext(),"device_mac",device_mac.toUpperCase(Locale.ROOT));
                Toast.ShowSuccess(linspirer_fakeuploader.this, "?????????");
            }
        });
    }
    private void scrolldown(){
        ScrollView sv=findViewById(R.id.scrollview1);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });    }
}