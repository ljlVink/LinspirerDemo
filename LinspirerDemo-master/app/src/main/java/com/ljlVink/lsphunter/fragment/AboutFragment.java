package com.ljlVink.lsphunter.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.huosoft.wisdomclass.linspirerdemo.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.aboutme)
                .addItem(new Element().setTitle("Version:"+ BuildConfig.VERSION_NAME))
                .addItem(new Element().setTitle("Github").setOnClickListener(view -> urlstart("https://github.com/ljlVink/LinspirerDemo")))
                .addItem(new Element().setTitle("HackMdm Core").setOnClickListener(view -> urlstart("https://github.com/ljlVink/hackmdm-core")))
                .addItem(new Element().setTitle("官网").setOnClickListener(view -> urlstart("https://youngtoday.github.io/")))
                .setDescription("Linspirer Hunter(原Linspirer Demo),比领创更懂你的MDM").create();

    }
    private void urlstart(String url){
        Uri uri=Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(uri);
        try {
            getContext().startActivity(intent);
        } catch (Exception ignore) {
        }

    }

}