package com.ljlVink.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.huosoft.wisdomclass.linspirerdemo.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        Element adsElement = new Element();
        adsElement.setTitle("关于");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.aboutme)
                .addItem(new Element().setTitle("Version:"+ BuildConfig.VERSION_NAME))
                .addItem(new Element().setTitle("QQ群").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        urlstart("https://jq.qq.com/?_wv=1027&k=r1tpCVuO");
                    }
                }))
                .addItem(new Element().setTitle("Github").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        urlstart("https://github.com/ljlVink/LinspirerDemo");
                    }
                }))
                .addItem(new Element().setTitle("反馈bug").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        urlstart("https://github.com/ljlVink/LinspirerDemo/issues/new");
                    }
                }))
                .addItem(new Element().setTitle("官网").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        urlstart("https://youngtoday.github.io/");

                    }
                }))
                .setDescription("Linspirer Demo,比领创更懂你的MDM")
                .create();
        setContentView(aboutPage);

    }
    private void urlstart(String url){
        Uri uri=Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(uri);
        try {
            startActivity(intent);
        } catch (Exception e) {
        }

    }
}