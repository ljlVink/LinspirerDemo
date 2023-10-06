package com.ljlVink.lsphunter;


import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.huosoft.wisdomclass.linspirerdemo.R;
import com.ljlVink.lsphunter.fragment.AboutFragment;
import com.ljlVink.lsphunter.fragment.AppManageFragment;
import com.ljlVink.lsphunter.fragment.Homefragment;
import com.ljlVink.lsphunter.fragment.LinspirerPWDFragment;
import com.ljlVink.lsphunter.fragment.SettingsFragment;
import com.ljlVink.lsphunter.fragment.WebviewFragment;

public class SecondFragment extends Fragment {

    public static SecondFragment newInstance(int menuItemId) {
        // 根据一级菜单项创建二级菜单Fragment
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt("menuItemId", menuItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_level_menu,container,false);
        int pageId=(int)getArguments().get("menuItemId");
        Context ctx=getContext();
        switch (pageId) {
            case 0:
                view  = inflater.inflate(R.layout.fragment_home, container, false);
                Homefragment home=new Homefragment(view,ctx);
                home.HandleFragment();

                break;
            case 3:
                view =inflater.inflate(R.layout.activity_app_manage_new,container,false);
                AppManageFragment appManageFragment=new AppManageFragment(view,ctx);
                appManageFragment.HandleFragment();
                break;
            case 10:
                view =inflater.inflate(R.layout.activity_settings,container,false);
                SettingsFragment settings=new SettingsFragment();
                FragmentManager fm=getChildFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.lspdemo_settings,settings);
                transaction.commit();
                break;

            case 12:
                view = inflater.inflate(R.layout.activity_webview,container,false);
                WebviewFragment wbview=new WebviewFragment(view,ctx);
                wbview.HandleWebView("https://youngtoday.github.io/docs/intro");
                break;
            case 13:
                view = inflater.inflate(R.layout.activity_webview,container,false);
                WebviewFragment wbview2=new WebviewFragment(view,ctx);
                wbview2.HandleWebView("http://tools-vue.zuoyebang.com/static/hy/tools-vue/calculator.html");
                break;
            case 15:
                view =inflater.inflate(R.layout.activity_linspirer_pwdcalc,container,false);
                LinspirerPWDFragment linspirerPWDFragment=new LinspirerPWDFragment(view,ctx);
                linspirerPWDFragment.HandleFragment();
                break;
            case 20:
                view =inflater.inflate(R.layout.activity_about,container,false);
                FragmentTransaction transaction1 = getChildFragmentManager().beginTransaction();
                AboutFragment aboutFragment=new AboutFragment();
                transaction1.replace(R.id.about_page,aboutFragment);
                transaction1.commit();
                break;


        }

        return view;
    }
}
