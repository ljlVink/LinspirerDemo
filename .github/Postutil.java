package com.ljlVink.core.core;

import android.content.Context;

import com.huosoft.wisdomclass.linspirerdemo.BuildConfig;
import com.ljlVink.core.core.IPostcallback;
import com.ljlVink.utils.Toast;

public class Postutil {
    Context context;
    public Postutil(Context context){
        this.context=context;
    }
    public void getAnnouncement(final IPostcallback callback) {
        callback.onSuccess(
                "正在使用git自编译版本,仅限个人使用\n"+
                "version:"+BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE+")\n"+
                "Git commit Version:"+BuildConfig.BUILD_GITVER+"\n"+
                "BuildTime"+BuildConfig.BUILD_DATE);
    }

    public void CloudAuthorize(){
        return;
    }
    public void sendPost(String opt) {
       Toast.ShowInfo(context,"正在使用git自编译版本,仅限个人使用,Version:"+BuildConfig.VERSION_NAME+","+BuildConfig.BUILD_GITVER);
    }
    public void SwordPlan(){
        Toast.ShowInfo(context,"正在使用git自编译版本,仅限个人使用,Version:"+BuildConfig.VERSION_NAME+","+BuildConfig.BUILD_GITVER);
    }
}