package com.huosoft.wisdomclass.linspirerdemo;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ljlVink.MDM;
import com.ljlVink.utils.DataCleanManager;
import com.ljlVink.utils.FileUtils;
import com.ljlVink.utils.Toast;
import com.ljlVink.utils.appsecurity.AppEntranceChecker;
import com.ljlVink.utils.appsecurity.ROM_identifier;
import com.ljlVink.utils.Signutil;
import com.tencent.bugly.Bugly;

public class lspdemoApplication extends Application {
    private static int MMDM=100;
    private static lspdemoApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        DataCleanManager.clearAllCache(this);
        mApplication = this;
        MMDM=new MDM(this).MDM();
        new ROM_identifier(this).checkrom();
        int keystatus = new Signutil(this, "97:8D:89:23:F9:F3:AF:C9:A3:79:37:2C:C8:A6:FF:A8:26:CC:DE:EF").f();
        if (!new AppEntranceChecker(this).checkEntrance()){
            throw new Error();
        }
        if(!new AppEntranceChecker(this).checkPMProxy()){
            throw new Error();
        }
        if ( 666 == keystatus) {
            Bugly.init(this, "50a36059a3", false);
        }else{
            Toast.ShowInfo(this,"请使用官方渠道安装包进行安装,否则将收不到更新!");
        }
        FileUtils.getInstance(this).copyAssetsToSD("apk","lspdemo.apks");
    }
    public static Context getApplication() {
        return mApplication;
    }
    public static int getMMDM() {
        return MMDM;
    }


}