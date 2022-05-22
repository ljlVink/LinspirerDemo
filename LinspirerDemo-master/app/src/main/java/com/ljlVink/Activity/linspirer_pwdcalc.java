package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.linspirerfake.ICallback;
import com.ljlVink.linspirerfake.PostUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class linspirer_pwdcalc extends AppCompatActivity {


        String key500 = "1191ADF1-8489-D8DA-5E9B-755A8B674394-485SDEWQ-QWYHK586";
        String key501 = "40E06F51-30D0-D6AD-7F7D-008AD0ADC570";
        public void OnClick(final View view) {
        try {
            switch (view.getId()) {
                case R.id.button: {
                    TextView tv3 = (TextView) findViewById(R.id.tv3);
                    tv3.setText("");
                    String swdid = ((EditText)this.findViewById(R.id.et)).getText().toString();
                    String pass500 = calc(swdid,key500);
                    String pass501 = calc(swdid,key501);
                    TextView tvs = (TextView) findViewById(R.id.tv);
                    tvs.setText("5.0.***:"+pass500+"\n5.01.***:"+pass501);
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("id","1");
                    jsonObject.put("!version","1");
                    jsonObject.put("jsonrpc","1.0");
                    jsonObject.put("is_encrypt",false);
                    jsonObject.put("client_version","Vtongyongshengchan_4.3.1");
                    jsonObject.put("method","com.linspirer.whitelist.whitepass");
                    JSONObject para=new JSONObject();
                    para.put("swdid",swdid.toLowerCase(Locale.ROOT));
                    para.put("schoolid","127df023-de3d-4bca-b810-b2b51854a064");
                    jsonObject.put("params",para);
                    new PostUtils().sendPost(jsonObject, "https://cloud.linspirer.com:883/public-interface.php", new ICallback() {
                        @Override
                        public void callback(String str) {
                            String passwd="";
                            try{
                                JSONObject obj= JSON.parseObject(str);
                                JSONObject obj1=obj.getJSONObject("data");
                                JSONObject obj2=obj1.getJSONObject("adminpass");
                                passwd=obj2.getString("password");
                            }catch (Exception e){
                                passwd="未查询到设备密码,请检查设备是否合法";
                            }
                            final String psw2=passwd;
                            tv3.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv3.setText(psw2);
                                }});
                        }
                    });
                    break;
                }
            }
        }
        catch (Exception ex) {}
    }
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_linspirer_pwdcalc);
        }

	/*
	 领创apk里扣的代码 微改
	 renamed from: com.innofidei.guardsecure.util.c
	 loaded from: classes.dex
	 */
   public static String calc(String str,String key) {
        String str2 = new SimpleDateFormat("yyyyMMdd").format(new Date()) + str + key;
        String b = m3759b(str2);
        if (b.length() > 8) {
            return m3758c(b.substring(b.length() - 8, b.length()));
        }
        return "";
    }

        /* renamed from: b */
        public static String m3759b(String str) {
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance("md5");
            instance.reset();
            instance.update(bytes);
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 255);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

        /* renamed from: c */
        public static String m3758c(String str) {
        String valueOf = String.valueOf(Long.parseLong(str, 16));
        if (valueOf.length() <= 8) {
            return valueOf;
        }
        return valueOf.substring(valueOf.length() - 8, valueOf.length());
    }
    }

