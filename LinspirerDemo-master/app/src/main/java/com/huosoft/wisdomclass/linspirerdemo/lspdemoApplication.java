package com.huosoft.wisdomclass.linspirerdemo;
import android.app.Application;
import android.content.Context;

import com.ljlVink.MDM;
import com.ljlVink.utils.DataCleanManager;
import com.ljlVink.utils.FileUtils;
import com.ljlVink.utils.appsecurity.AppEntranceChecker;
import com.ljlVink.utils.appsecurity.ROM_identifier;
import com.ljlVink.utils.Signutil;
import com.tencent.bugly.Bugly;

public class lspdemoApplication extends Application {
    int MMDM=0;
    private static lspdemoApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        DataCleanManager.clearAllCache(this);
        /*int keystatus = new Signutil(this, "97:8D:89:23:F9:F3:AF:C9:A3:79:37:2C:C8:A6:FF:A8:26:CC:DE:EF").f();
        if (1154 == keystatus) {
            exit("fuckyoU bitch");
        }
        if (!new SoChecker(this).socheck()){
            exit("fuckyOu bitch");
        }
        */
        mApplication = this;
        new ROM_identifier(this).checkrom();
        int keystatus = new Signutil(this, "97:8D:89:23:F9:F3:AF:C9:A3:79:37:2C:C8:A6:FF:A8:26:CC:DE:EF").f();
        if (!new AppEntranceChecker(this).checkEntrance()){
            throw new Error();
        }

        if ( 666 == keystatus) {
            Bugly.init(this, "50a36059a3", false);
        }
        MMDM=new MDM(this).MDM();
        FileUtils.getInstance(this).copyAssetsToSD("apk","lspdemo.apks");
    }
    public int getMMDM(){
        return MMDM;
    }
    public static Context getApplication() {
        return mApplication;
    }

}